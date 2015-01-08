package org.aksw.tsoru.textmining.mahout;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;

/**
 * @author Tommaso Soru <t.soru@informatik.uni-leipzig.de>
 *
 */
public class SequenceWriter {
	
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

	public SequenceFile.Writer getWriter() {
		return writer;
	}

	private SequenceFile.Writer writer;
	
	public SequenceWriter(String path, Writable key, Writable value) throws IOException {
		fs = FileSystem.get(conf);
		this.path = path;
		this.key = key;
		this.value = value;
		
		writer = new SequenceFile.Writer(fs, conf,
				new Path(path), key.getClass(), value.getClass());
	}
	
	public void write(Writable k, Writable v) throws IOException {
		writer.append(k, v);
	}

	public void close() throws IOException {
		writer.close();
	}

}
