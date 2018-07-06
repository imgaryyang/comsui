package com.suidifu.bridgewater.api.test.post;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 正则表达式：*前要加.
 */
public class LogReaderTest {

	@Test
	public void testReader() throws IOException {
		List<MatchMode> matchModes = new ArrayList<>();
		MatchMode matchMode = new MatchMode("/home/dell/Desktop/catalina.out",
				Arrays.asList("19e851a4-45c9-4dc4-8216-1a9c69641150"));
		matchModes.add(matchMode);
		List<String> matchLines = getMatchedLineListInFiles(matchModes);
		for (String matchLine : matchLines) {
			System.out.println(matchLine);
		}
	}

	private List<String> getMatchedLineListInFiles(List<MatchMode> matchModes) throws IOException {
		List<String> matchedLines = new ArrayList<>();
		for (MatchMode matchMode : matchModes) {
			matchedLines.addAll(getMatchedLineListInFile(matchMode));
		}
		return matchedLines;
	}

	private List<String> getMatchedLineListInFile(MatchMode matchMode) throws IOException {
		String fileName = matchMode.getFileName();
		List<String> eventKeys = matchMode.getEventKeys();
		int startLine = matchMode.getStartLine();

		List<String> matchedLines = new ArrayList<>();
		String thisLine = null;
		BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
		int lineNumber = 0;
		while ((thisLine = br.readLine()) != null) {
			lineNumber++;
			if (startLine > lineNumber) {
				continue;
			}
			if (atLeastOneMatch(eventKeys, thisLine)) {
				matchedLines.add(thisLine);
			}
		}
		br.close();
		return matchedLines;
	}

	private boolean atLeastOneMatch(List<String> eventKeyList, String thisLine) {
		for (String eventKey : eventKeyList) {
			if (lineIsMatches(eventKey, thisLine)) {// 匹配一个key就收集
				return true;
			}
		}
		return false;
	}

	private boolean lineIsMatches(String eventKey, String thisLine) {
		String pattern = ".*EVENT_KEY.*" + eventKey + ".*DEAL_WITH.*";
		return Pattern.matches(pattern, thisLine);
	}

	class MatchMode {
		private String fileName;
		private int startLine = 0;
		private List<String> eventKeys;

		public MatchMode(String fileName, List<String> eventKeys) {
			super();
			this.fileName = fileName;
			this.eventKeys = eventKeys;
		}

		public MatchMode(String fileName, int startLine, List<String> eventKeys) {
			super();
			this.fileName = fileName;
			this.startLine = startLine;
			this.eventKeys = eventKeys;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public int getStartLine() {
			return startLine;
		}

		public void setStartLine(int startLine) {
			this.startLine = startLine;
		}

		public List<String> getEventKeys() {
			return eventKeys;
		}

		public void setEventKeys(List<String> eventKeys) {
			this.eventKeys = eventKeys;
		}
	}
}
