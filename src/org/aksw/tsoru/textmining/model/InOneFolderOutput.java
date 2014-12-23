package org.aksw.tsoru.textmining.model;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class InOneFolderOutput extends Output {

	@Override
	public String getExt() {
		// TODO Auto-generated method stub
		return ".txt";
	}

	@Override
	public String getHeader() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String preprocess(String body) {
		// TODO Auto-generated method stub
		return body;
	}

}
