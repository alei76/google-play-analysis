package org.aksw.tsoru.textmining;

import io.LoadData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import data.AndroidApp;
import data.Review;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class AppCorrelation {

	public static void main(String[] args) throws FileNotFoundException {
		
		HashMap<String, AndroidApp> appdata = new HashMap<String, AndroidApp>();
		ArrayList<AndroidApp> data = LoadData.loadData("data/", true);
		for(AndroidApp app : data)
			appdata.put(app.getId(), app);
		
		Scanner in = new Scanner(new File("output.csv"));
		PrintWriter pw = new PrintWriter(new File("correlation_50.csv"));
		
		in.nextLine(); // skip titles
		pw.write("APP_ID,RATING,POLARITY\n");
		
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
			double posPol = Integer.parseInt(app[5]);
			double negPol = Integer.parseInt(app[6]);
			double sumPol = posPol + negPol;
			
			if(sumPol == 0.0)
				continue;
			
			double polarity = (sumPol == 0.0) ? 0.0 : (posPol - negPol) / sumPol;
			
			System.out.println(appID +"\t" + polarity);
			
			AndroidApp a = appdata.get(appID);
			
			if(a == null) {
				System.out.println("NOT FOUND: " + appID);
				continue;
			}
			
			List<Review> list = a.getReviews();
			double sum = 0.0;
			for(Review r : list) {
				double rd = 0.0;
				try {
					sum += Double.parseDouble(r.getRating());
				} catch (NumberFormatException e) {
					// HTML string instead of raw value
					rd = Double.parseDouble(r.getRating().split("%")[0]);
					sum += rd;
				}
			}

			String rating = "" + (sum / a.getReviews().size());

			String string = appID+","+rating+","+polarity+","+posPol+","+negPol+"\n";
			pw.write(string);
			
			
		}
		
		in.close();
		pw.close();
		
		System.out.println("Done.");
		
	}

}
