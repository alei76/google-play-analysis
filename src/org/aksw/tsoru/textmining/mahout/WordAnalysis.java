package org.aksw.tsoru.textmining.mahout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.aksw.tsoru.textmining.utils.DataIO;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class WordAnalysis {
	
	private static final int TOP_WORDS = 80;
	private static final long FILE_SIZE_THRESHOLD = 0;
	
	static HashMap<String, Value> partition = new HashMap<String, Value>();
	static TreeSet<String> multi = new TreeSet<String>();

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		
		String input = args[0];
		
//		String input = "etc/output-k5-x10/";
//		String input = "etc/output-k10-x20/";
		
		File folder = new File(input);
		File[] files = folder.listFiles();
		
		List<File> list = new ArrayList<>(); 

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile() && files[i].getName().startsWith("wordcloud") && files[i].length() > FILE_SIZE_THRESHOLD) {
				System.out.println("File " + files[i].getName());
				process(input + "/" + files[i].getName());
				list.add(files[i]);
			}
		}
		
		for(File f : list) {
			String fname = f.getPath();
			System.out.println("\nWORDS FOR FILE "+fname+"\n=====================");
			for(String key : partition.keySet()) {
				Value v = partition.get(key);
				if(v.filename.equals(fname))
					System.out.println(key+"\t"+v.count);
			}
		}

	}

	private static void process(String filepath) throws FileNotFoundException, ClassNotFoundException, IOException {
		HashMap<String, Integer> map = DataIO.readMap(filepath);
		System.out.println("words loaded: " + map.size());
		int i = 0;
		for(String key : map.keySet()) {
			if(!multi.contains(key)) {
				if(partition.containsKey(key)) {
					partition.remove(key);
					multi.add(key);
				} else
					partition.put(key, new Value(map.get(key), filepath));
			}
//			if(key.equals("fast")) {
//				System.out.println(partition.get(key).filename + "\t" + partition.get(key).count);
//				System.exit(0);
//			}
			if(++i == TOP_WORDS)
				break;
		}
	}

}

class Value {
	Integer count;
	String filename;
	Value(Integer count, String index) {
		this.count = count;
		this.filename = index;
	}
}
