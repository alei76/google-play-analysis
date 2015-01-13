package org.aksw.tsoru.textmining.model;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class InFoldersOutput extends Output {

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
		// all reviews end with " Full Review" (length=12)
		body = body.substring(0, body.length() - 12);
		return body;
	}

}
