package org.aksw.tsoru.textmining.model;

import java.util.HashMap;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class Cluster implements Comparable<Cluster> {
	
	private String name;
	
	private HashMap<String, Integer> wordcount;
	
	public Cluster(String name) {
		super();
		this.name = name;
		wordcount = new HashMap<>();
	}

	public String getName() {
		return name;
	}
	
	public void increment(String word, int k) {
		Integer n = wordcount.get(word);
		wordcount.put(word, n == null ? k : n + k);
	}
	
	public HashMap<String, Integer> getWordcount() {
		return wordcount;
	}

	@Override
	public int compareTo(Cluster o) {
		return this.name.compareTo(o.name);
	}
	
}
