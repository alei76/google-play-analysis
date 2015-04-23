package org.aksw.tsoru.textmining;

import io.LoadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.aksw.tsoru.textmining.model.InFoldersOutput;
import org.aksw.tsoru.textmining.model.InOneFolderOutput;
import org.aksw.tsoru.textmining.model.Output;

import data.AndroidApp;
import data.Review;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class DatasetBuilder {
		
	public static void main(String[] args) throws FileNotFoundException {
		
//		DatasetBuilder.build("data/", "etc/reviews", new RawOutput());
//		DatasetBuilder.build("data/", "etc/reviews", new CSVOutput());
//		DatasetBuilder.build("data/", "etc/reviews", new ARFFOutput());
//		DatasetBuilder.build("data/", "etc/infolders", new InFoldersOutput());
		DatasetBuilder.build("data/", "etc/inonefolder", new InOneFolderOutput());
		
	}

	public static void build(String input, String output, Output type) throws FileNotFoundException {

		ArrayList<AndroidApp> data = LoadData.loadData(input, true);

		if(type instanceof InFoldersOutput) {
			new File(output).mkdirs();
			for(AndroidApp app : data) {
				new File(output+"/"+app.getCategory().replaceAll(" ", "_")).mkdirs();
				for(int i=0; i<app.getReviews().size(); i++) {
					Review rev = app.getReviews().get(i);
					PrintWriter pw = new PrintWriter(new File(output + "/"
							+ app.getCategory().replaceAll(" ", "_") + "/" + app.getId() + "_" + i + type.getExt()));
					pw.write(type.preprocess(rev.getBody()));
					pw.close();
				}
			}
			
		} else if(type instanceof InOneFolderOutput) {
			new File(output).mkdirs();
			for(AndroidApp app : data) {
				if(app.getReviews() == null) {
					System.out.println("SKIPPED: "+app.getId());
					continue;
				}
				for(int i=0; i<app.getReviews().size(); i++) {
					Review rev = app.getReviews().get(i);
					PrintWriter pw = new PrintWriter(new File(output + "/"
							+ app.getId() + "_" + i + type.getExt()));
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