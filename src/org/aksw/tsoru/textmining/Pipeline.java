package org.aksw.tsoru.textmining;

import org.aksw.tsoru.textmining.mahout.Centroids;
import org.aksw.tsoru.textmining.mahout.KMeansCentroids;
import org.aksw.tsoru.textmining.model.InOneFolderOutput;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class Pipeline {

	public static void main(String[] args) throws Exception {

		String prefix = args[0];
		
		DatasetBuilder.build("data/", "etc/inonefolder", new InOneFolderOutput());
		Centroids.copy(prefix);
		KMeansCentroids.main(new String[]{"0", prefix});
		
	}

}
