package peter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CommandSender implements Runnable {
	Application application;
	private static final String lineSeparator = System.getProperty("line.separator");
	private final BufferedWriter bw;

	public CommandSender(BufferedWriter bw, Application application) {
		this.bw = bw;
		this.application = application;
	}

	public void run() {
//		try {
//			final  = new BufferedWriter(new OutputStreamWriter(os), 50);
//			for (int i = 1; i >= 0; i--) {
//				bw.write("ls");
//				bw.write(lineSeparator);
//				bw.flush();
//			}
//			bw.close();
//		} catch (IOException e) {
//			throw new IllegalArgumentException("IOException sending data to child process.");
//		}
	}

}
