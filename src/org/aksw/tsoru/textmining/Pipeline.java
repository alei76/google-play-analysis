package org.aksw.tsoru.textmining;

import org.aksw.tsoru.textmining.mahout.KMeansCentroids;
import org.aksw.tsoru.textmining.model.InOneFolderOutput;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class Pipeline {

	public static void main(String[] args) throws Exception {
		
		DatasetBuilder.build("data/", "etc/inonefolder", new InOneFolderOutput());
		KMeansCentroids.main(new String[]{});
		
	}

}
