package org.aksw.tsoru.textmining;

import org.aksw.tsoru.textmining.mahout.Centroids;
import org.aksw.tsoru.textmining.mahout.FuzzyKMeansCentroids;
import org.aksw.tsoru.textmining.model.InOneFolderOutput;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Pipeline {

	public static void main(String[] args) throws Exception {

		String prefix = args[0];
		
		DatasetBuilder.build("data/", "etc/inonefolder", new InOneFolderOutput());
		
		Centroids.copyPosNeg(prefix);
		
//		KMeansCentroids.main(new String[]{"0", prefix});
		FuzzyKMeansCentroids.main(new String[]{"0", prefix, "10"});
//		CosSimApproach.main(new String[]{});
		
	}

}
