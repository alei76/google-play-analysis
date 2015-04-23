package org.aksw.tsoru.textmining.mahout;

import java.io.IOException;

import org.apache.hadoop.io.Text;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Search {

	public static void main(String[] args) throws IOException {
		sequence(args[0]);
	}

	/**
	 * Filename to file content.
	 * 
	 * @throws IOException
	 */
	public static void sequence(String word) throws IOException {
		SequenceReader reader = new SequenceReader("etc/sequence/chunk-0", new Text(), new Text());
		while(reader.next()) {
			if(((Text)reader.getValue()).find(word) > -1)
				System.out.println(word + "\t" + reader.getKey() + "\t" + reader.getValue());
		}
		reader.close();
	}

}
