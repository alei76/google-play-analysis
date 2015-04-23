package org.aksw.tsoru.textmining.mahout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.management.AttributeNotFoundException;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class ListClustered {

	public static void main(String[] args) throws AttributeNotFoundException, IOException {
		
		String output = args[0];
		
		String what = args[1];
		
		HashMap<String, String> map = ShowResults.fuzzyClusteredPoints(output);
		for(String key : map.keySet())
			if(map.get(key).equals(what)) {
				System.out.println(getReview("etc/inonefolder"+key));
			}
		
	}

	public static String getReview(String path) throws FileNotFoundException {
		String r = "";
		Scanner in = new Scanner(new File(path));
		while(in.hasNextLine())
			r += in.nextLine() + "\n";
		in.close();
		r += "\n";
		return r;
	}

}
