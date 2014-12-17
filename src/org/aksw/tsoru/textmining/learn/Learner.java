package org.aksw.tsoru.textmining.learn;


import java.io.File;
import java.io.PrintWriter;

import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class Learner {

	public static void main(String[] args) throws Exception {
		
		// etc/reviews1k.arff
		String testFile = args[0];
		DataSource trainds = new DataSource(testFile);
		Instances train = trainds.getDataSet();
		
		FilteredClusterer fc = new FilteredClusterer();
		StringToWordVector stwv = new StringToWordVector();
		SimpleKMeans model = new SimpleKMeans();
		model.setNumClusters(5);
		model.setPreserveInstancesOrder(true);
		fc.setClusterer(model);
		fc.setFilter(stwv);
		
		fc.buildClusterer(train);
		
		PrintWriter pw = new PrintWriter(new File(args[0]+".predictions"));
		for(Instance in : train) {
			System.out.println(fc.clusterInstance(in)+"\t"+in);
			pw.write(fc.clusterInstance(in)+"\n");
		}
		pw.close();
		
	}

}
