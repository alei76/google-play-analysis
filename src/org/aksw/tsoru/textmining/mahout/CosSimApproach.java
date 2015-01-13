package org.aksw.tsoru.textmining.mahout;

import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class CosSimApproach {
	
	private static String 	input = "etc/inonefolder",
							sequence = "etc/sequence",
							vectors = "etc/vectors";

	public static void main(String[] args) throws Exception {
		
		String[] centroids = {"bugginess", "performance_pos", "polarity_pos", "performance_neg", "polarity_neg"};
		run(centroids);
		
	}
	
	public static void run(String[] centroids) throws Exception {
		
		SequenceFilesFromDirectory s = new SequenceFilesFromDirectory();
		String[] seqparam = { "--input", input, "--output", sequence, "--overwrite",
				"--method", "sequential" };
		int seqresult = s.run(seqparam);
		System.out.println("sequence status: "+seqresult);
		String[] para = { "-o", vectors, "-i", sequence, "-wt", "tfidf", "-ow",
				"-ml", "50", "-ng", "2" };
		int vecresult = new SparseVectorsFromSequenceFiles().run(para);
		System.out.println("vector status: "+vecresult);
		
		CosineSim.run(vectors, centroids);
		
	}
		
}
