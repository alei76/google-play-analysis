package org.aksw.tsoru.textmining.mahout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import org.aksw.tsoru.textmining.utils.DataIO;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class WordAnalysis {
	
	private static final int TOP_WORDS = 20;
	private static final long FILE_SIZE_THRESHOLD = 2048;
	
	static HashMap<String, Value> partition = new HashMap<String, Value>();
	static TreeSet<String> multi = new TreeSet<String>();

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		
//		String input = "etc/output-k5-x10/";
		String input = "etc/output-k10-x20/";
		
		File folder = new File(input);
		File[] files = folder.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile() && files[i].length() > FILE_SIZE_THRESHOLD) {
				System.out.println("File " + files[i].getName());
				process(input + files[i].getName());
			}
		}
		
		for(String key : partition.keySet()) {
			Value v = partition.get(key);
			System.out.println(key+"\t"+v.filename+"\t"+v.count);
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
