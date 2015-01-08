package org.aksw.tsoru.textmining.mahout;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import org.aksw.tsoru.textmining.model.Cluster;
import org.aksw.tsoru.textmining.utils.DataIO;
import org.aksw.tsoru.textmining.utils.MapUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.classify.WeightedPropertyVectorWritable;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class ShowResults {

	public static void main(String[] args) throws IOException {
		
		String outputdir = args[0];
		
//		SequenceReader reader = new SequenceReader("etc/vectors/tfidf-vectors/part-r-00000", new Text(), new VectorWritable());
//		while(reader.next()) {
//			System.out.println(reader.getKey() + "\t" + reader.getValue());
//		}
//		reader.close();
//		clusters();
//
//        HashMap<String, Integer> clusterCount = ShowResults.centroidSizes();
//		
//        System.out.println("CLUSTERS\n========\nID\tSIZE");
//		for(String key : clusterCount.keySet())
//			System.out.println(key + "\t" + clusterCount.get(key));
		
		HashMap<String, String> clustering = clusteredPoints(outputdir);
		HashMap<Integer, String> dict = dictionary();
		
		TreeSet<String> clusterNames = new TreeSet<String>();
		for(String c : clustering.values())
			clusterNames.add(c);
		System.out.println(clusterNames.size() + " distinct clusters found.");
		
		HashMap<String, Cluster> clusters = new HashMap<String, Cluster>();
		for(String c : clusterNames)
			clusters.put(c, new Cluster(c));
		
		SequenceReader reader = new SequenceReader("etc/vectors/tf-vectors/part-r-00000", new Text(), new VectorWritable());
		while(reader.next()) {
			String name = clustering.get(reader.getKey().toString());
			Cluster cluster = clusters.get(name);
//			System.out.println(reader.getKey() + " belongs to cluster " + name);
			for(Element el : ((VectorWritable) reader.getValue()).get().nonZeroes()) {
				cluster.increment(dict.get(el.index()), (int) el.get());
//				System.out.println("\t" + dict.get(el.index()) + ":" + el.get());
			}
		}
		reader.close();
		
		for(String c : clusters.keySet()) {
//			System.out.println(MapUtils.sortByComparator(clusters.get(c).getWordcount(), false));
			HashMap<String, Integer> map = MapUtils.sortByComparator(clusters.get(c).getWordcount(), false);
			DataIO.serialize(map, outputdir + "/wordcloud_" + c);
			int i = 0;
			System.out.println("Top 100 words of cluster "+c);
			for(String word : map.keySet()) {
				System.out.println(word + ":" + map.get(word));
				if(++i == 100)
					break;
			}
		}
		
	}
	
	/**
	 * Get centroids and their sizes.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static HashMap<String, Integer> centroidSizes(String outputdir) throws IOException {
        HashMap<String, Integer> clusterCount = new HashMap<String, Integer>();
        for(int i=0; i<=0; i++) {
        	String suffix = (i<10) ? "0"+i : ""+i;
			SequenceReader reader = new SequenceReader(outputdir + "/clusteredPoints/part-m-000" + suffix, new IntWritable(), new WeightedPropertyVectorWritable());
//			System.out.println(reader.getKey() + "\t" + instanceName(reader.getValue()));
			while(reader.next()) {
				String centroID = reader.getKey().toString();
				Integer v = clusterCount.get(centroID);
				clusterCount.put(centroID, v == null ? 1 : v + 1);
			}
			reader.close();
		}
		return clusterCount;
	}

	/**
	 * Instance filename to cluster ID.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static HashMap<String, String> clusteredPoints(String outputdir) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        for(int i=0; i<=0; i++) {
        	String suffix = (i<10) ? "0"+i : ""+i;
			SequenceReader reader = new SequenceReader(outputdir + "/clusteredPoints/part-m-000" + suffix, new IntWritable(), new WeightedPropertyVectorWritable());
			while(reader.next()) {
	//			System.out.println(reader.getKey() + "\t" + instanceName(reader.getValue()));
				map.put(instanceName(reader.getValue()), reader.getKey().toString());
			}
			reader.close();
        }
		return map;
	}

	private static String instanceName(Writable value) {
		return ((NamedVector)((WeightedPropertyVectorWritable) value).getVector()).getName();
	}

	/**
	 * Filename to ???
	 * 
	 * @throws IOException
	 */
	public static void clusters() throws IOException {
		SequenceReader reader = new SequenceReader("etc/clusters/part-randomSeed", new Text(), new ClusterWritable());
		while(reader.next()) {
			System.out.println(reader.getKey() + "\t" + reader.getValue());
		}
		reader.close();
	}

	/**
	 * ID to word.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static HashMap<Integer, String> dictionary() throws IOException {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		SequenceReader reader = new SequenceReader("etc/vectors/dictionary.file-0", new Text(), new IntWritable());
		while(reader.next()) {
//			System.out.println(reader.getKey() + "\t" + reader.getValue());
			map.put(((IntWritable) reader.getValue()).get(), reader.getKey().toString());
		}
		reader.close();
		return map;
	}

	/**
	 * Filename to file content.
	 * 
	 * @throws IOException
	 */
	public static void sequence() throws IOException {
		SequenceReader reader = new SequenceReader("etc/sequence/chunk-0", new Text(), new Text());
		while(reader.next()) {
			System.out.println(reader.getKey() + "\t" + reader.getValue());
		}
		reader.close();
	}

	/**
	 * Filename to file content.
	 * 
	 * @throws IOException
	 */
	public static HashMap<String, VectorWritable> tfidf() throws IOException {
		HashMap<String, VectorWritable> map = new HashMap<String, VectorWritable>();
		SequenceReader reader = new SequenceReader("etc/vectors/tfidf-vectors/part-r-00000", new Text(), new VectorWritable());
		while(reader.next()) {
			System.out.println(reader.getKey() + "\t" + reader.getValue());
			String k = reader.getKey().toString();
			if(k.equals("/pos.txt") || k.equals("/neg.txt"))
				map.put(k, new VectorWritable(((VectorWritable) reader.getValue()).get()));
		}
		reader.close();
		return map;
	}

}
