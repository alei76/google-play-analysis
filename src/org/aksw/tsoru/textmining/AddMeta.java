package org.aksw.tsoru.textmining;

import io.LoadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import data.AndroidApp;
import data.Review;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class AddMeta {

	public static void main(String[] args) throws FileNotFoundException {
		
		HashMap<String, AndroidApp> appdata = new HashMap<String, AndroidApp>();
		ArrayList<AndroidApp> data = LoadData.loadData("data/", true);
		for(AndroidApp app : data)
			appdata.put(app.getId(), app);
		
		Scanner in = new Scanner(new File("output.csv"));
		PrintWriter pw = new PrintWriter(new File("metadata.csv"));
		
		in.nextLine(); // skip titles
		pw.write("APP_ID,CATEGORY,RATING\n");
		
		while(in.hasNextLine()) {
			
			String line;
			try {
				line = in.nextLine();
			} catch (NoSuchElementException e1) {
				// end of file
				break;
			}
			
			String[] app = line.split(",");
			String appID = app[0];
			
			AndroidApp a = appdata.get(appID);
			
			if(a == null) {
				System.out.println("NOT FOUND: " + appID);
				continue;
			}
			
			String cat = a.getCategory();
			
			double sum = 0.0;
			for(Review r : a.getReviews())
				try {
					sum += Double.parseDouble(r.getRating());
				} catch (NumberFormatException e) {
					// HTML string instead of raw value
					double d = Double.parseDouble(r.getRating().split("%")[0]);
//					System.out.println(r.getRating() + " is "+d);
					sum += d;
				}
			
			String rating = "" + (sum / a.getReviews().size());
			
			String string = appID+","+cat+","+rating+"\n";
			pw.write(string);
			
		}
		
		in.close();
		pw.close();
		
		System.out.println("Done.");
		
	}

}
