package org.aksw.tsoru.textmining.mahout;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;

/**
 * @author Tommaso Soru <tsoru@informatik.uni-leipzig.de>
 *
 */
public class SequenceReader {
	
	private Configuration conf = new Configuration();
	private FileSystem fs;
	private String path;
	private Writable key, value;

	public String getPath() {
		return path;
	}

	public Writable getKey() {
		return key;
	}

	public Writable getValue() {
		return value;
	}

	public SequenceFile.Reader getReader() {
		return reader;
	}

	private SequenceFile.Reader reader;
	
	public SequenceReader(String path, Writable key, Writable value) throws IOException {
		fs = FileSystem.get(conf);
		this.path = path;
		this.key = key;
		this.value = value;
		
		reader = new SequenceFile.Reader(fs, 
				new Path(path), conf);
	}
	
	public boolean next() throws IOException {
		return reader.next(key, value);
	}

	public void close() throws IOException {
		reader.close();
	}

}
