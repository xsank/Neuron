package org.neuron.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {

	private Logger logger;
	private static Level level = Level.ALL;
	private static String name = "Neuron";
	private static String separator = " ";
	private static MyLogger instance = new MyLogger(name, level);

	private MyLogger(String name, Level lv) {
		logger = Logger.getLogger(name);
		level = lv;
		logger.setLevel(lv);
	}

	public Logger getLogger() {
		return logger;
	}

	private static String summaryLog(Object... text) {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		for (Object t : text) {
			sb.append(separator);
			sb.append(t);
		}
		return sb.toString();
	}

	public static void log(Object... text) {
		String s = summaryLog(text);
		instance.getLogger().log(level, s);
	}

	public static void log(Level lv, Object... text) {
		String s = summaryLog(text);
		instance.getLogger().log(lv, s);
	}

	public static void fineLog(Object... text) {
		log(Level.FINE,text);
	}

	public static void infoLog(Object... text) {
		log(Level.INFO,text);
	}

	public static void warningLog(Object... text) {
		log(Level.WARNING,text);
	}

	public static void severeLog(Object... text) {
		log(Level.SEVERE,text);
	}
}
