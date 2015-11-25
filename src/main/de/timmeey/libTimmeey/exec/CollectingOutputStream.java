package de.timmeey.libTimmeey.exec;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.io.output.TeeOutputStream;

public class CollectingOutputStream implements Runnable {

	private final List<String> lines = new LinkedList<String>();

	BufferedReader reader;

	private final PipedOutputStream outBranched = new PipedOutputStream();
	private PipedInputStream inBranched;

	private OutputStreamWriter outWriter;

	public CollectingOutputStream(final PipedOutputStream out) throws IOException {
		reader = new BufferedReader(new InputStreamReader(new PipedInputStream(out)));

		new Thread(this, "CollectionOutput").start();

	}

	private void processLine(String line) throws IOException {
		if (!(inBranched == null && outWriter == null)) {
			outWriter.write(line + "\n");
			outWriter.flush();
		}
		lines.add(line);
	}

	public List<String> getLines() {

		return lines;
	}

	@Override
	public void run() {
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				processLine(line);
			}
			if (!(inBranched == null && outWriter == null)) {

				this.outWriter.close();
			}
			this.reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public PipedInputStream getStream() throws IOException {
		if (inBranched == null && outWriter == null) {
			inBranched = new PipedInputStream(outBranched);
			this.outWriter = new OutputStreamWriter(outBranched);
		}

		return this.inBranched;
	}

}