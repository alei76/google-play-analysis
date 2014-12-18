package org.aksw.tsoru.textmining;

import io.LoadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.aksw.tsoru.textmining.model.ARFFOutput;
import org.aksw.tsoru.textmining.model.CSVOutput;
import org.aksw.tsoru.textmining.model.MapReduceOutput;
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
		
//		DatasetBuilder.build("data/", "etc/reviews", new RawOutput());
//		DatasetBuilder.build("data/", "etc/reviews", new CSVOutput());
//		DatasetBuilder.build("data/", "etc/reviews", new ARFFOutput());
		DatasetBuilder.build("data/", "etc/mapreduce", new MapReduceOutput());
		
	}

	private static void build(String input, String output, Output type) throws FileNotFoundException {

		ArrayList<AndroidApp> data = LoadData.loadData(input, true);

		if(type instanceof MapReduceOutput) {
			new File(output).mkdirs();
			for(AndroidApp app : data) {
				new File(output+"/"+app.getCategory()).mkdirs();
				for(Review rev : app.getReviews()) {
					PrintWriter pw = new PrintWriter(new File(output + "/"
							+ app.getCategory() + "/" + app.getId() + type.getExt() + type.getExt()));
					pw.write(type.preprocess(rev.getBody()));
					pw.close();
				}
			}
			
		} else {
			PrintWriter pw = new PrintWriter(new File(output + type.getExt()));
			pw.write(type.getHeader());
			for(AndroidApp app : data)
				for(Review rev : app.getReviews())
					pw.write(type.preprocess(rev.getBody()));
			pw.close();
		}
		
	}

}