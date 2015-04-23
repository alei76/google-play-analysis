package org.aksw.tsoru.textmining.model;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public abstract class Output {
	
	public Output() {
		super();
	}
	
	public abstract String getExt();

	public abstract String getHeader();

	public abstract String preprocess(String body);
	
}