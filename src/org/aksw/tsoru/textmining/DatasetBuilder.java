package org.aksw.tsoru.textmining;

import io.LoadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import data.AndroidApp;
import data.Review;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class DatasetBuilder {

	public static void main(String[] args) throws FileNotFoundException {
		
		DatasetBuilder.build("data/", "etc/reviews.arff");
		
	}

	private static void build(String input, String output) throws FileNotFoundException {
		
		PrintWriter pw = new PrintWriter(new File(output));
		pw.write(getHeader());
		ArrayList<AndroidApp> data =  LoadData.loadData(input, true);
		for(AndroidApp app : data) {
			for(Review rev : app.getReviews()) {
				pw.write(preprocess(rev.getBody()));
			}
		}
		pw.close();
		
	}

	private static String getHeader() {
		return "@relation reviews\n\n@attribute body string\n\n@data\n";
	}

	private static String preprocess(String body) {
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

}
