package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import data.AndroidApp;

public class LoadData {

	// just specify your path here if you want otherwise folder apps in the
	// prject folder

	/**
	 * 
	 * this class read all the json in a given folder and return a list of
	 * AbdroidApp
	 * 
	 * 
	 * @param path
	 * @param numFiles
	 *            if 0 readAll, if not read the given file
	 * @return
	 */
	public static ArrayList<AndroidApp> loadData(String path, boolean verbose) {
		ArrayList<AndroidApp> data = new ArrayList<AndroidApp>();
		// coutn how many files
		int cont = 0;

		long bytes = 0;
		final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		// Load form Json file
		File[] files = new File(path).listFiles(new FileFilter() {			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".json");
			}
		});
		int numberFiles = files.length;

		System.out.println("");

		for (File file : files) {
			if (file.isFile()) {

				try {
					BufferedReader br;

					br = new BufferedReader(new FileReader(
							file.getAbsolutePath()));

					bytes = bytes + file.length();
					final ArrayList<AndroidApp> fileData = gson.fromJson(br,
							new TypeToken<ArrayList<AndroidApp>>() {
							}.getType());
					data.addAll(fileData);
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cont++;

				System.out.println("done = " + (double) cont * 100 / numberFiles + "%");

			}
		}
		if (verbose) {
			System.out.println(data.size()
					+ " apps loaded and ready to be analysed");
			int size = (int) (bytes / 1000000.0);

			System.out.println(size + " MegaBytes of data loaded in memory");
		}

		return data;

	}

	
	/**
	 * it gets the list of android id per category
	 * 
	 * 
	 * @param path
	 * @return
	 */
	public static HashMap<String, ArrayList<String>> loadId(String path) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		File[] files = new File(path).listFiles();
		System.out.println("reading files in : " + path);

		// for each file get the name
		for (File file : files) {
			if (file.isFile()) {
				try {
					BufferedReader br;
					System.out.println("reading file" + file.getName());
					br = new BufferedReader(new FileReader(
							file.getAbsolutePath()));
					ArrayList<String> ids = new ArrayList<String>();
					String line;
					while ((line = br.readLine()) != null) {
						ids.add(line);
					}

					map.put(file.getName().replace(".txt", ""), ids);
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return map;
	}

	/**
	 * load data in a single file
	 * @param file
	 * @param b
	 * @return
	 */
	public static ArrayList<AndroidApp> loadDataSingleFile(File file, boolean b) {
		ArrayList<AndroidApp> data = new ArrayList<AndroidApp>();

		final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		try {
			BufferedReader br;

			br = new BufferedReader(new FileReader(file.getAbsolutePath()));
			final ArrayList<AndroidApp> fileData = gson.fromJson(br,
					new TypeToken<ArrayList<AndroidApp>>() {
					}.getType());
			data.addAll(fileData);
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
