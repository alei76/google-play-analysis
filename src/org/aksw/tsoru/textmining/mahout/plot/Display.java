package org.aksw.tsoru.textmining.mahout.plot;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.AbstractCluster;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.UncommonDistributions;
import org.apache.mahout.clustering.classify.ClusterClassifier;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.clustering.display.DisplayClustering;
import org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansDriver;
import org.apache.mahout.clustering.fuzzykmeans.SoftCluster;
import org.apache.mahout.clustering.iterator.ClusterIterator;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.iterator.FuzzyKMeansClusteringPolicy;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.HadoopUtil;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirValueIterable;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

public class Display extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8241356777855452269L;

	private static final Logger log = LoggerFactory
			.getLogger(DisplayClustering.class);

	protected static final int DS = 72; // default scale = 72 pixels per inch

	protected static final int SIZE = 8; // screen size in inches

	private static final Collection<Vector> SAMPLE_PARAMS = Lists
			.newArrayList();

	protected static final List<VectorWritable> SAMPLE_DATA = Lists
			.newArrayList();

	protected static final List<List<Cluster>> CLUSTERS = Lists.newArrayList();

	static final Color[] COLORS = { Color.red, Color.orange, Color.yellow,
			Color.green, Color.blue, Color.magenta, Color.lightGray };

	protected static final double T1 = 3.0;

	protected static final double T2 = 2.8;

	static double significance = 0.05;

	protected static int res; // screen resolution

	public static void main(String[] args) throws Exception {
		DistanceMeasure measure = new ManhattanDistanceMeasure();

		Path samples = new Path("samples");
		Path output = new Path("etc/output-performance-x10");
		Configuration conf = new Configuration();
		HadoopUtil.delete(conf, output);
		HadoopUtil.delete(conf, samples);
		RandomUtils.useTestSeed();
		generateSamples();
		writeSampleData(samples);
		boolean runClusterer = false;
		int maxIterations = 10;
		float threshold = 0.001F;
		float m = 1.1F;
		if (runClusterer) {
			runSequentialFuzzyKClusterer(conf, samples, output, measure,
					maxIterations, m, threshold);
		} else {
			int numClusters = 3;
			runSequentialFuzzyKClassifier(conf, samples, output, measure,
					numClusters, maxIterations, m, threshold);
		}
		
		loadClustersWritable(output);
		
		Display d = new Display();
		try {
			// retrieve image
			BufferedImage bi = new BufferedImage(d.getWidth(), d.getHeight(), BufferedImage.TYPE_INT_ARGB);
			d.paint(bi.createGraphics());
		    File outputfile = new File("saved.png");
		    ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	public void initialize() {
		// Get screen resolution
		res = Toolkit.getDefaultToolkit().getScreenResolution();

		// Set Frame size in inches
		this.setSize(SIZE * res, SIZE * res);
		this.setVisible(true);
		this.setTitle("Asymmetric Sample Data");

		// Window listener to terminate program.
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	Display() {
		initialize();
		this.setTitle("Fuzzy k-Means Clusters (>" + (int) (significance * 100)
				+ "% of population)");
	}

	// Override the paint() method
	@Override
	public void paint(Graphics g) {
		plotSampleData((Graphics2D) g);
		plotClusters((Graphics2D) g);
	}

	private static void runSequentialFuzzyKClassifier(Configuration conf,
			Path samples, Path output, DistanceMeasure measure,
			int numClusters, int maxIterations, float m, double threshold)
			throws IOException {
		Collection<Vector> points = Lists.newArrayList();
		for (int i = 0; i < numClusters; i++) {
			points.add(SAMPLE_DATA.get(i).get());
		}
		List<Cluster> initialClusters = Lists.newArrayList();
		int id = 0;
		for (Vector point : points) {
			initialClusters.add(new SoftCluster(point, id++, measure));
		}
		ClusterClassifier prior = new ClusterClassifier(initialClusters,
				new FuzzyKMeansClusteringPolicy(m, threshold));
		Path priorPath = new Path(output, "classifier-0");
		prior.writeToSeqFiles(priorPath);

		ClusterIterator.iterateSeq(conf, samples, priorPath, output,
				maxIterations);
		loadClustersWritable(output);
		
		prior.close();
	}

	private static void runSequentialFuzzyKClusterer(Configuration conf,
			Path samples, Path output, DistanceMeasure measure,
			int maxIterations, float m, double threshold) throws IOException,
			ClassNotFoundException, InterruptedException {
		Path clustersIn = new Path(output, "random-seeds");
		RandomSeedGenerator.buildRandom(conf, samples, clustersIn, 3, measure);
		FuzzyKMeansDriver.run(samples, clustersIn, output, threshold,
				maxIterations, m, true, true, threshold, true);

		loadClustersWritable(output);
	}

	protected static void plotClusters(Graphics2D g2) {
		int cx = CLUSTERS.size() - 1;
		for (List<Cluster> clusters : CLUSTERS) {
			g2.setStroke(new BasicStroke(cx == 0 ? 3 : 1));
			g2.setColor(COLORS[Math.min(COLORS.length - 1, cx--)]);
			for (Cluster cluster : clusters) {
				plotEllipse(g2, cluster.getCenter(),
						cluster.getRadius().times(3));
			}
		}
	}

	protected static void plotSampleParameters(Graphics2D g2) {
		Vector v = new DenseVector(2);
		Vector dv = new DenseVector(2);
		g2.setColor(Color.RED);
		for (Vector param : SAMPLE_PARAMS) {
			v.set(0, param.get(0));
			v.set(1, param.get(1));
			dv.set(0, param.get(2) * 3);
			dv.set(1, param.get(3) * 3);
			plotEllipse(g2, v, dv);
		}
	}

	protected static void plotSampleData(Graphics2D g2) {
		double sx = (double) res / DS;
		g2.setTransform(AffineTransform.getScaleInstance(sx, sx));

		// plot the axes
		g2.setColor(Color.BLACK);
		Vector dv = new DenseVector(2).assign(SIZE / 2.0);
		plotRectangle(g2, new DenseVector(2).assign(2), dv);
		plotRectangle(g2, new DenseVector(2).assign(-2), dv);

		// plot the sample data
		g2.setColor(Color.DARK_GRAY);
		dv.assign(0.03);
		for (VectorWritable v : SAMPLE_DATA) {
			plotRectangle(g2, v.get(), dv);
		}
	}

	/**
	 * This method plots points and colors them according to their cluster
	 * membership, rather than drawing ellipses.
	 * 
	 * As of commit, this method is used only by K-means spectral clustering.
	 * Since the cluster assignments are set within the eigenspace of the data,
	 * it is not inherent that the original data cluster as they would in
	 * K-means: that is, as symmetric gaussian mixtures.
	 * 
	 * Since Spectral K-Means uses K-Means to cluster the eigenspace data, the
	 * raw output is not directly usable. Rather, the cluster assignments from
	 * the raw output need to be transferred back to the original data. As such,
	 * this method will read the SequenceFile cluster results of K-means and
	 * transfer the cluster assignments to the original data, coloring them
	 * appropriately.
	 * 
	 * @param g2
	 * @param data
	 */
	protected static void plotClusteredSampleData(Graphics2D g2, Path data) {
		double sx = (double) res / DS;
		g2.setTransform(AffineTransform.getScaleInstance(sx, sx));

		g2.setColor(Color.BLACK);
		Vector dv = new DenseVector(2).assign(SIZE / 2.0);
		plotRectangle(g2, new DenseVector(2).assign(2), dv);
		plotRectangle(g2, new DenseVector(2).assign(-2), dv);

		// plot the sample data, colored according to the cluster they belong to
		dv.assign(0.03);

		Path clusteredPointsPath = new Path(data, "clusteredPoints");
		Path inputPath = new Path(clusteredPointsPath, "part-m-00000");
		Map<Integer, Color> colors = new HashMap<Integer, Color>();
		int point = 0;
		for (Pair<IntWritable, WeightedVectorWritable> record : new SequenceFileIterable<IntWritable, WeightedVectorWritable>(
				inputPath, new Configuration())) {
			int clusterId = record.getFirst().get();
			VectorWritable v = SAMPLE_DATA.get(point++);
			Integer key = clusterId;
			if (!colors.containsKey(key)) {
				colors.put(key,
						COLORS[Math.min(COLORS.length - 1, colors.size())]);
			}
			plotClusteredRectangle(g2, v.get(), dv, colors.get(key));
		}
	}

	/**
	 * Identical to plotRectangle(), but with the option of setting the color of
	 * the rectangle's stroke.
	 * 
	 * NOTE: This should probably be refactored with plotRectangle() since most
	 * of the code here is direct copy/paste from that method.
	 * 
	 * @param g2
	 *            A Graphics2D context.
	 * @param v
	 *            A vector for the rectangle's center.
	 * @param dv
	 *            A vector for the rectangle's dimensions.
	 * @param color
	 *            The color of the rectangle's stroke.
	 */
	protected static void plotClusteredRectangle(Graphics2D g2, Vector v,
			Vector dv, Color color) {
		double[] flip = { 1, -1 };
		Vector v2 = v.times(new DenseVector(flip));
		v2 = v2.minus(dv.divide(2));
		int h = SIZE / 2;
		double x = v2.get(0) + h;
		double y = v2.get(1) + h;

		g2.setStroke(new BasicStroke(1));
		g2.setColor(color);
		g2.draw(new Rectangle2D.Double(x * DS, y * DS, dv.get(0) * DS, dv
				.get(1) * DS));
	}

	/**
	 * Draw a rectangle on the graphics context
	 * 
	 * @param g2
	 *            a Graphics2D context
	 * @param v
	 *            a Vector of rectangle center
	 * @param dv
	 *            a Vector of rectangle dimensions
	 */
	protected static void plotRectangle(Graphics2D g2, Vector v, Vector dv) {
		double[] flip = { 1, -1 };
		Vector v2 = v.times(new DenseVector(flip));
		v2 = v2.minus(dv.divide(2));
		int h = SIZE / 2;
		double x = v2.get(0) + h;
		double y = v2.get(1) + h;
		g2.draw(new Rectangle2D.Double(x * DS, y * DS, dv.get(0) * DS, dv
				.get(1) * DS));
	}

	/**
	 * Draw an ellipse on the graphics context
	 * 
	 * @param g2
	 *            a Graphics2D context
	 * @param v
	 *            a Vector of ellipse center
	 * @param dv
	 *            a Vector of ellipse dimensions
	 */
	protected static void plotEllipse(Graphics2D g2, Vector v, Vector dv) {
		double[] flip = { 1, -1 };
		Vector v2 = v.times(new DenseVector(flip));
		v2 = v2.minus(dv.divide(2));
		int h = SIZE / 2;
		double x = v2.get(0) + h;
		double y = v2.get(1) + h;
		g2.draw(new Ellipse2D.Double(x * DS, y * DS, dv.get(0) * DS, dv.get(1)
				* DS));
	}

	protected static void generateSamples() {
		generateSamples(500, 1, 1, 3);
		generateSamples(300, 1, 0, 0.5);
		generateSamples(300, 0, 2, 0.1);
	}

	protected static void generate2dSamples() {
		generate2dSamples(500, 1, 1, 3, 1);
		generate2dSamples(300, 1, 0, 0.5, 1);
		generate2dSamples(300, 0, 2, 0.1, 0.5);
	}

	/**
	 * Generate random samples and add them to the sampleData
	 * 
	 * @param num
	 *            int number of samples to generate
	 * @param mx
	 *            double x-value of the sample mean
	 * @param my
	 *            double y-value of the sample mean
	 * @param sd
	 *            double standard deviation of the samples
	 */
	protected static void generateSamples(int num, double mx, double my,
			double sd) {
		double[] params = { mx, my, sd, sd };
		SAMPLE_PARAMS.add(new DenseVector(params));
		log.info("Generating {} samples m=[{}, {}] sd={}", num, mx, my, sd);
		for (int i = 0; i < num; i++) {
			SAMPLE_DATA.add(new VectorWritable(new DenseVector(new double[] {
					UncommonDistributions.rNorm(mx, sd),
					UncommonDistributions.rNorm(my, sd) })));
		}
	}

	protected static void writeSampleData(Path output) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(output.toUri(), conf);
		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, output,
				Text.class, VectorWritable.class);
		try {
			int i = 0;
			for (VectorWritable vw : SAMPLE_DATA) {
				writer.append(new Text("sample_" + i++), vw);
			}
		} finally {
			Closeables.close(writer, false);
		}
	}

	protected static List<Cluster> readClustersWritable(Path clustersIn) {
		List<Cluster> clusters = Lists.newArrayList();
		Configuration conf = new Configuration();
		for (ClusterWritable value : new SequenceFileDirValueIterable<ClusterWritable>(
				clustersIn, PathType.LIST, PathFilters.logsCRCFilter(), conf)) {
			Cluster cluster = value.getValue();
			log.info("Reading Cluster:{} center:{} numPoints:{} radius:{}",
					cluster.getId(),
					AbstractCluster.formatVector(cluster.getCenter(), null),
					cluster.getNumObservations(),
					AbstractCluster.formatVector(cluster.getRadius(), null));
			clusters.add(cluster);
		}
		return clusters;
	}

	protected static void loadClustersWritable(Path output) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(output.toUri(), conf);
		for (FileStatus s : fs.listStatus(output, new ClustersFilter())) {
			List<Cluster> clusters = readClustersWritable(s.getPath());
			CLUSTERS.add(clusters);
		}
	}

	/**
	 * Generate random samples and add them to the sampleData
	 * 
	 * @param num
	 *            int number of samples to generate
	 * @param mx
	 *            double x-value of the sample mean
	 * @param my
	 *            double y-value of the sample mean
	 * @param sdx
	 *            double x-value standard deviation of the samples
	 * @param sdy
	 *            double y-value standard deviation of the samples
	 */
	protected static void generate2dSamples(int num, double mx, double my,
			double sdx, double sdy) {
		double[] params = { mx, my, sdx, sdy };
		SAMPLE_PARAMS.add(new DenseVector(params));
		log.info("Generating {} samples m=[{}, {}] sd=[{}, {}]", num, mx, my,
				sdx, sdy);
		for (int i = 0; i < num; i++) {
			SAMPLE_DATA.add(new VectorWritable(new DenseVector(new double[] {
					UncommonDistributions.rNorm(mx, sdx),
					UncommonDistributions.rNorm(my, sdy) })));
		}
	}

	protected static boolean isSignificant(Cluster cluster) {
		return (double) cluster.getNumObservations() / SAMPLE_DATA.size() > significance;
	}

}

final class ClustersFilter implements PathFilter {
	@Override
	public boolean accept(Path path) {
		String pathString = path.toString();
		return pathString.contains("/clusters-");
	}
}