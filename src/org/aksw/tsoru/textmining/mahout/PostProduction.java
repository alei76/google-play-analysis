package org.aksw.tsoru.textmining.mahout;

import java.io.IOException;

import javax.management.AttributeNotFoundException;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class PostProduction {

	public static void main(String[] args) throws AttributeNotFoundException, IOException, ClassNotFoundException {
		
		String output = args[0];
		
		ShowResults.main(new String[]{output});
		
		WordAnalysis.main(new String[]{output});

	}
	
}
