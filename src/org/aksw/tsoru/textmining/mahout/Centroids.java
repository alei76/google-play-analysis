package org.aksw.tsoru.textmining.mahout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.Cluster;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Centroids {

	public static void main(String[] args) throws IOException {
		Centroids.read();
//		Centroids.generate();
	}


	private static void read() throws IOException {
		SequenceReader reader = new SequenceReader("etc/centroids/part-r-00000", new Text(), new ClusterWritable());
		while(reader.next()) {
			Vector v = ((ClusterWritable) reader.getValue()).getValue().getCenter();
			System.out.println(reader.getKey() + "\t" + v);
		}
		reader.close();
	}
	
	/**
	 * @param posID
	 * @param negID
	 * @throws IOException 
	 */
	public static void set(String prefix, String posID, String negID) throws IOException {
		HashMap<String, VectorWritable> map = ShowResults.getTfidf(posID, negID);
		VectorWritable pos = map.get(posID);
		Vector vpos = pos.get();
		VectorWritable neg = map.get(negID);
		Vector vneg = neg.get();
		Cluster cpos = new Kluster(vpos, 101, new EuclideanDistanceMeasure());
		Cluster cneg = new Kluster(vneg, 202, new EuclideanDistanceMeasure());
		
		SequenceWriter writer = new SequenceWriter("etc/centroids/part-r-00000", new Text(), new ClusterWritable());
		writer.write(new Text("/"+prefix+"_pos.centroid"), new ClusterWritable(cpos));
		writer.write(new Text("/"+prefix+"_neg.centroid"), new ClusterWritable(cneg));
		writer.close();
	}

	public static void generate(String prefix) throws IOException {
		HashMap<String, VectorWritable> map = ShowResults.tfidf(prefix);
//		System.out.println(map);
				
		VectorWritable pos = map.get("/"+prefix+"_pos.txt");
		Vector vpos = pos.get();
//		int nwords = vpos.size();
		VectorWritable neg = map.get("/"+prefix+"_neg.txt");
		Vector vneg = neg.get();
//		Vector vneu = new DenseVector(nwords);
		
//		double avg = 0.0;
//		int nonzero = 0;
//		for(int j=0; j<nwords; j++) {
//			double d = vpos.getElement(j).get();
//			if(d > 0.0) {
//				avg += d;
//				nonzero++;
////				System.out.println(nonzero + ") " + d + " -> " + avg);
//			}
//			double e = vneg.getElement(j).get();
//			if(e > 0.0) {
//				avg += e;
//				nonzero++;
////				System.out.println(nonzero + ") " + e + " -> " + avg);
//			}
//		}
//			
//		avg = avg / nonzero / 100.0;
//		
//		System.out.println("Avg = "+avg);
//		
//		for(int i=0; i<nwords; i++)
//			if(vpos.getElement(i).get() > 0.0 && vneg.getElement(i).get() > 0.0)
//				vneu.set(i, avg);

		Cluster cpos = new Kluster(vpos, 101, new EuclideanDistanceMeasure());
		Cluster cneg = new Kluster(vneg, 202, new EuclideanDistanceMeasure());
//		Cluster cneu = new Kluster(vneu, 303, new EuclideanDistanceMeasure());
		
		SequenceWriter writer = new SequenceWriter("etc/centroids/part-r-00000", new Text(), new ClusterWritable());
		writer.write(new Text("/"+prefix+"_pos.centroid"), new ClusterWritable(cpos));
		writer.write(new Text("/"+prefix+"_neg.centroid"), new ClusterWritable(cneg));
//		writer.write(new Text("/"+prefix+"_neu.centroid"), new ClusterWritable(cneu));
		writer.close();
	
	}


	public static void copyPosNeg(String prefix) throws IOException {
		Files.copy(new File("centroids/"+prefix+"_pos.txt").toPath(), new File("etc/inonefolder/"+prefix+"_pos.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(new File("centroids/"+prefix+"_neg.txt").toPath(), new File("etc/inonefolder/"+prefix+"_neg.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	public static void copy(String centroid) throws IOException {
		Files.copy(new File("centroids/"+centroid+".txt").toPath(), new File("etc/inonefolder/"+centroid+".txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

}
