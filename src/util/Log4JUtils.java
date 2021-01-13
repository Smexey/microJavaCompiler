package util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

public class Log4JUtils {

	private static Log4JUtils logs = new Log4JUtils();

	public static Log4JUtils instance() {
		return logs;
	}

	public URL findLoggerConfigFile() {
		try {
			return new File("config/log4j.xml").toURI().toURL();
		} catch (MalformedURLException e) {
			System.out.println("missing config file");
		}
		return null;
	}

	public void prepareLogFile(Logger root) {
		Appender appender = root.getAppender("file");

		if (!(appender instanceof FileAppender))
			return;
		FileAppender fAppender = (FileAppender) appender;

		String logFileName = fAppender.getFile();

		File logFile = new File(logFileName);
		File renamedFile = new File(logFile.getAbsolutePath().substring(0) + System.currentTimeMillis() + ".log");

		// if (logFile.exists()) {
		// logFile = renamedFile;
		// }

		fAppender.setFile(logFile.getAbsolutePath());
		fAppender.activateOptions();
	}

}
