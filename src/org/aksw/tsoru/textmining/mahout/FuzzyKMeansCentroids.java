package org.aksw.tsoru.textmining.mahout;

import java.util.HashMap;

import org.apache.mahout.clustering.fuzzykmeans.FuzzyKMeansDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class FuzzyKMeansCentroids {
	
	private static String 	input = "etc/inonefolder",
							sequence = "etc/sequence",
							vectors = "etc/vectors",
							centroids = "etc/centroids",
							output = "etc/output-";

	/**
	 *  Assumed <i>[prefix]_pos.txt</i> and <i>[prefix]_neg.txt</i> as copied into: <i>etc/inonefolder/</i>.
	 */
	public static void main(String[] args) throws Exception {
		
		int arg = Integer.parseInt(args[0]);
		String prefix = args[1];
		String numIter = args[2];
		output += prefix + "-x" + numIter;
		
		if(arg <= 0) {
			SequenceFilesFromDirectory s = new SequenceFilesFromDirectory();
			String[] seqparam = { "--input", input, "--output", sequence, "--overwrite",
					"--method", "sequential" };
			int seqresult = s.run(seqparam);
			System.out.println("sequence status: "+seqresult);
		}
		
		if(arg <= 1) {
			String[] para = { "-o", vectors, "-i", sequence, "-wt", "tfidf", "-ow",
					"-ml", "50", "-ng", "2" };
			int vecresult = new SparseVectorsFromSequenceFiles().run(para);
			System.out.println("vector status: "+vecresult);
		}
		
		if(arg <= 2) {
			
//			Centroids.generate(prefix);
			Centroids.set(prefix, "/com.cadTouch.androidTrial_19.txt", "/com.globalmotion.everytrail_126.txt");
			
			String[] fkmparam = {"-i", vectors + "/tfidf-vectors", "-c", centroids, "-o", output,
					"-x", numIter, "-m", "2", "-cl"};
			FuzzyKMeansDriver d = new FuzzyKMeansDriver();
			int fkmresult = d.run(fkmparam);
			System.out.println("fuzzy k-means status: "+fkmresult);

		}
		
		if(arg <= 3) {

	        HashMap<String, Integer> clusterCount = ShowResults.centroidSizes(output);
			
	        System.out.println("CLUSTERS\n========\nID\tSIZE");
			for(String key : clusterCount.keySet())
				System.out.println(key + "\t" + clusterCount.get(key));
			System.out.println();
			
//			ShowResults.fuzzyClusteredPoints(output);
			
			PostProduction.main(new String[]{output});
		}
		
	}
		

//	{
//		int minSupport = 5;
//		int minDf = 5;
//		long maxDF = 99;
//		int maxNGramSize = 2;
//		int minLLRValue = 50;
//		int reduceTasks = 1;
//		int chunkSize = 200;
//		float norm = -1;
//		boolean sequentialAccessOutput = true;
//		String inputDir = "etc/infolders";
////		File inputDirFile = new File(inputDir);
//		Configuration conf = new Configuration();
//		FileSystem fs = FileSystem.get(conf);
//		String outputDir = "clusters";
//		HadoopUtil.delete(conf, new Path(outputDir));
//		Path tokenizedPath = new Path(outputDir,
//				DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER);
//		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
//		DocumentProcessor.tokenizeDocuments(new Path(inputDir), analyzer
//				.getClass().asSubclass(Analyzer.class), tokenizedPath, conf);
//		DictionaryVectorizer.createTermFrequencyVectors(tokenizedPath,
//				new Path(outputDir), "output-vectors", conf, minSupport, maxNGramSize,
//				minLLRValue, 2, true, reduceTasks, chunkSize,
//				sequentialAccessOutput, false);
//		Path input = new Path(outputDir,
//				DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER);
//		Path output = new Path(outputDir);
//		
//		TFIDFConverter.processTfIdf(input, output, conf, TFIDFConverter.calculateDF(input, 
//				output, conf, 500), minDf, maxDF, norm, true,
//				sequentialAccessOutput, false, reduceTasks);
//		Path vectorsFolder = new Path(outputDir, "tfidf-vectors");
//		Path centroids = new Path(outputDir, "centroids");
//		Path clusterOutput = new Path(outputDir, "clusters");
//		RandomSeedGenerator.buildRandom(conf, vectorsFolder, centroids, 20,
//				new CosineDistanceMeasure());
//		KMeansDriver.run(conf, vectorsFolder, centroids, clusterOutput,
//				0.01, 20, true, 0.1, false);
//		SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
//				clusterOutput, Cluster.CLUSTERED_POINTS_DIR + "/part-m-00000"),
//				conf);
//		analyzer.close();
//	}

}
