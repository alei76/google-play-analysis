package org.aksw.tsoru.textmining;

import io.LoadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.aksw.tsoru.textmining.model.ARFFOutput;
import org.aksw.tsoru.textmining.model.CSVOutput;
import org.aksw.tsoru.textmining.model.Output;
import org.aksw.tsoru.textmining.model.RawOutput;

import data.AndroidApp;
import data.Review;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class DatasetBuilder {
		
	public static void main(String[] args) throws FileNotFoundException {
		
		DatasetBuilder.build("data/", "etc/reviews", new RawOutput());
		DatasetBuilder.build("data/", "etc/reviews", new CSVOutput());
		DatasetBuilder.build("data/", "etc/reviews", new ARFFOutput());
		
	}

	private static void build(String input, String output, Output type) throws FileNotFoundException {
		
		PrintWriter pw = new PrintWriter(new File(output) + type.getExt());
		pw.write(type.getHeader());
		ArrayList<AndroidApp> data =  LoadData.loadData(input, true);
		for(AndroidApp app : data) {
			for(Review rev : app.getReviews()) {
				pw.write(type.preprocess(rev.getBody()));
			}
		}
		pw.close();
		
	}

}