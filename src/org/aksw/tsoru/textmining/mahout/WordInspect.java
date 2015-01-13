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
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class WordInspect {

	private static final int TOP_WORDS = 10000;

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		String input = args[0];
		
		File folder = new File(input);
		File[] files = folder.listFiles();
		
		List<File> list = new ArrayList<>(); 

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile() && files[i].getName().startsWith("wordcloud")) {
//				System.out.println("File " + files[i].getName());
				inspect(args[1], input + "/" + files[i].getName());
				list.add(files[i]);
			}
		}
		
	}

	private static void inspect(String word, String filepath) throws FileNotFoundException, ClassNotFoundException, IOException {
		
		HashMap<String, Value> partition = new HashMap<String, Value>();
		TreeSet<String> multi = new TreeSet<String>();

		HashMap<String, Integer> map = DataIO.readMap(filepath);
//		System.out.println("words loaded: " + map.size());
		int i = 0;
		for(String key : map.keySet()) {
			if(!multi.contains(key)) {
				if(partition.containsKey(key)) {
					partition.remove(key);
					multi.add(key);
				} else
					partition.put(key, new Value(map.get(key), filepath));
			}
			if(key.equals(word)) {
				System.out.println(word + "\t" + partition.get(key).filename + "\t" + partition.get(key).count + "\t#" + i);
				return;
			}
			if(++i == TOP_WORDS)
				break;
		}
	}


}
