package engine;

import io.LoadData;

import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.AndroidApp;
import data.Review;


/**
RAW DATA ANALYSIS
**/
public class GetSelectiveData {

	public static void main(String[] args) {

        firstPass(args);
		secondPass(args);
	}

	public static void firstPass(String[] args) {

		// TODO Auto-generated method stub
		HashMap<String, ArrayList<String>> top500id = LoadData.loadId(args[0] + "/");
		HashMap<String, ArrayList<AndroidApp>> resultMap = new HashMap<String, ArrayList<AndroidApp>>();

		// init the result
		for (String category : top500id.keySet()) {

			resultMap.put(category, new ArrayList<AndroidApp>());
		}

		System.out.println("starting read all data");

		int count = 0;
		int found = 0;
		// get all list of files
		File[] files = new File(args[0] + "/apps/").listFiles();

		// for each file get the data
		for (File file : files) {
			if (file.isFile()) {

				ArrayList<AndroidApp> data = LoadData.loadDataSingleFile(file,
						false);

				for (int j = 0; j < data.size(); j++) {

					// for each category find app
					for (String category : top500id.keySet()) {
						ArrayList<String> appIdlist = top500id.get(category);

						if (appIdlist.contains(data.get(j).getId())) {
							appIdlist.remove(data.get(j).getId());
							// System.out.println("found " + data.get(j) +
							// " category :"
							// + category + "to go : "
							// + (500 - resultMap.get(category).size()));
							found++;
							resultMap.get(category).add(data.get(j));
						}
					}
				}
				count = count + data.size();
				System.out.println("analyzed apps " + count + "found " + found);

				data.clear();
			}
		}
		final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		for (String category : top500id.keySet()) {
			final String json = gson.toJson(resultMap.get(category));

			try {
				final FileWriter writer = new FileWriter(args[0] + "/output/"
						+ category + "-DATA.json");
				writer.write(json);
				writer.close();

			} catch (final IOException e) {
				e.printStackTrace();
			}

			System.out.println("not found " + category + "  "
					+ top500id.get(category).size());

		}

	}

	public static void secondPass(String[] args) {

		// TODO Auto-generated method stub
		HashMap<String, ArrayList<String>> top500id = LoadData.loadId(args[0]
				+ "/");

		// result
		HashMap<String, ArrayList<AndroidApp>> resultMap = new HashMap<String, ArrayList<AndroidApp>>();

		// init the result
		for (String category : top500id.keySet()) {

			resultMap.put(category, new ArrayList<AndroidApp>());
		}

		System.out.println("starting read existing data");
		ArrayList<AndroidApp> existingData = LoadData.loadData(args[0]
				+ "/output/", false);
		System.out.println("existing data read");

		HashSet<String> idExisitngs = new HashSet<String>();
		for (AndroidApp app : existingData) {

			idExisitngs.add(app.getId());
		}

		File[] files = new File(args[0] + "/apps/").listFiles();
		int found = 0;
		int count = 0;
		// for each file get the data
		for (File file : files) {
			if (file.isFile()) {

				ArrayList<AndroidApp> data = LoadData.loadDataSingleFile(file,
						false);

				for (int j = 0; j < data.size(); j++) {

					// for each category find app
					for (String category : top500id.keySet()) {
						ArrayList<String> appIdlist = top500id.get(category);

						if (appIdlist.contains(data.get(j).getId())) {

							if (idExisitngs.contains(data.get(j).getId())) {
								// already have need to update
								for (AndroidApp app : existingData) {
									if (app.getId().equals(data.get(j).getId())) {

										if (app.getReviews() == null) {

											app.setReviews(new ArrayList<Review>());
										}
										app.getReviews().addAll(
												data.get(j).getReviews());
										app.setNumReviewsCollected(app
												.getReviews().size());
										resultMap.get(category).add(app);
										found++;


										break;
									}
								}

							} else {
								// new
								found++;
								resultMap.get(category).add(data.get(j));

							}
							appIdlist.remove(data.get(j).getId());
						}
					}
				}
				count = count + data.size();
				data.clear();
				System.out.println("analyzed apps " + count + "found " + found);
			}
		}
		final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		// convert java object to JSON format,
		// and returned as JSON formatted string
		for (String category : top500id.keySet()) {
			final String json = gson.toJson(resultMap.get(category));

			try {
				final FileWriter writer = new FileWriter(args[0] + "/output2/"
						+ category + "-DATA.json");
				writer.write(json);
				writer.close();

			} catch (final IOException e) {
				e.printStackTrace();
			}

			System.out.println("not found " + category + "  "
					+ top500id.get(category).size());


//			System.out.println("not found " + category + "  "
//					+ top500id.get(category).toString());
		}

	}

}
