package org.aksw.tsoru.textmining.model;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class CSVOutput extends Output {

	@Override
	public String getExt() {
		return ".csv";
	}

	@Override
	public String preprocess(String body) {
		// all reviews end with " Full Review" (length=12)
		body = body.substring(0, body.length() - 12);
		// one result per line
		body = body.replaceAll("\n", " ");
		// strip out non-valid characters
		body = body.replaceAll("[^\\x20-\\x7e]", " ");
		// quotes
		body = "\"" + body.replaceAll("\"", "'") + "\"";
		// carriage return
		body = body + "\n";
		return body;
	}

	@Override
	public String getHeader() {
		return "\"body\"\n";
	}

}
