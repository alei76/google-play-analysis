package org.aksw.tsoru.textmining;

import org.aksw.tsoru.textmining.mahout.Centroids;
import org.aksw.tsoru.textmining.mahout.CosSimApproach;
import org.aksw.tsoru.textmining.model.InOneFolderOutput;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class CosSimPipeline {

	public static void main(String[] args) throws Exception {
		
		String[] centroids = {"bugginess", "performance_pos", "performance_neg", "polarity_pos", "polarity_neg"};
		
		DatasetBuilder.build("data/", "etc/inonefolder", new InOneFolderOutput());
		
		for(String c : centroids)
			Centroids.copy(c);
		
		CosSimApproach.run(centroids);
		
	}

}
