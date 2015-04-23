package org.aksw.tsoru.textmining.mahout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class CosineSim {

	public static void main(String[] args) throws IOException {
		
		String vectors = args[0];
		String[] centroids = {"performance_pos", "performance_neg"};
		
		run(vectors, centroids);
		
	}
	
	public static void run(String vectors, String[] centroids) throws IOException {
	
		int l = centroids.length;
		
		Vector[] vct = new Vector[l];
		
		SequenceReader r2 = new SequenceReader(vectors + "/tfidf-vectors/part-r-00000", new Text(), new VectorWritable());
		while(r2.next()) {
			for(int i=0; i<l; i++)
				if(r2.getKey().toString().equals("/"+centroids[i]+".txt"))
					vct[i] = ((VectorWritable) r2.getValue()).get();
		}
		r2.close();
				
		int[] count = new int[l];
		
		PrintWriter[] pw = new PrintWriter[l];
		for(int i=0; i<l; i++)
			pw[i] = new PrintWriter(new File("cosinesim_"+centroids[i]+".txt"));
		
		HashMap<String, int[]> map = new HashMap<String, int[]>();
		
		SequenceReader reader = new SequenceReader(vectors + "/tfidf-vectors/part-r-00000", new Text(), new VectorWritable());
		outer: while(reader.next()) {
			String review = reader.getKey().toString();
			String notxt = review.substring(1, review.length() - 4);
			for(String c : centroids)
				if(c.equals(notxt))
					continue outer;
			String appID = notxt.substring(0, notxt.lastIndexOf("_"));

			if(!map.containsKey(appID))
				map.put(appID, new int[6]);
			int[] val = map.get(appID);
			val[0]++;
			
			Vector v = ((VectorWritable) reader.getValue()).get();
			double[] sim = new double[l];
			for(int i=0; i<l; i++)
				sim[i] = cosineSimilarity(v, vct[i]);

			// bugginess
			Added bug = handle(review, val, count, sim, pw, 0);
			if(bug == Added.Pos)
				val[1]++;
			
			// performance
			Added per = handle(review, val, count, sim, pw, 1, 2);
			if(per == Added.Pos)
				val[2]++;
			if(per == Added.Neg)
				val[3]++;
			
			// polarity
			Added pol = handle(review, val, count, sim, pw, 3, 4);
			if(pol == Added.Pos)
				val[4]++;
			if(pol == Added.Neg)
				val[5]++;
			
			map.put(appID, val);
			
		}
		reader.close();
		
		PrintWriter csv = new PrintWriter(new File("output.csv"));
		csv.write("APP_ID, NUM_REVIEWS, BUGGINESS, PERF_LVL_POS, PERF_LVL_NEG, POLARITY_POS, POLARITY_NEG\n");
		for(String appID : map.keySet()) {
			String string = appID;
			for(int n : map.get(appID))
				string += "," + n;
			csv.write(string + "\n");
		}
		csv.close();
		
		for(PrintWriter pwr : pw)
			pwr.close();
		
		for(int i=0; i<l; i++)
			System.out.println(centroids[i] + "=" + count[i]);
		
	}
	
	private static Added handle(String review, int[] val, int[] count, double[] sim,
			PrintWriter[] pw, int i) throws FileNotFoundException {
		if(sim[i] >= 0.1) {
			pw[i].write(review + "\n" + ListClustered.getReview("etc/inonefolder"+review) + sim[i] + "\n===============\n");
			count[i]++;
			return Added.Pos;
		}
		return Added.None;
	}

	private static Added handle(String review, int[] val, int[] count, double[] sim, PrintWriter[] pw, int i, int j) throws FileNotFoundException {
		if(sim[i] >= 0.1 || sim[j] >= 0.1) {
			if(sim[i] == sim[j])
				return Added.None;
			if(sim[i] > sim[j]) {
				pw[i].write(review + "\n" + ListClustered.getReview("etc/inonefolder"+review) + sim[i] + "\t" + sim[j] + "\n===============\n");
				count[i]++;
				return Added.Pos;
			} else {
				pw[j].write(review + "\n" + ListClustered.getReview("etc/inonefolder"+review) + sim[i] + "\t" + sim[j] + "\n===============\n");
				count[j]++;
				return Added.Neg;
			}
		}
		return Added.None;
	}

	private static double cosineSimilarity(Vector a, Vector b) {
		return a.dot(b) / (a.norm(2) * b.norm(2));
	}

}

enum Added {
	Pos, Neg, None;
}