package com.soundcloud.util;

import static com.soundcloud.util.Constants.TAB_CHAR;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility char for writing the result as tabbed separated words
 * 
 * @author Ashish
 *
 */
public class FileUtil {

	private static Function<List<String>, String> listToTabbedStringFn = list -> list.stream()
			.collect(Collectors.joining(TAB_CHAR));
	
	public static void writeToFile(List<List<String>> result, String outputFile) throws IOException {
		
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
			result.stream().map(listToTabbedStringFn).forEach(line -> writeToFile(writer, line));
		}
	}
	public static void writeToFileFromStream(Stream<List<String>> resultStream, String outputFile) throws IOException {
		
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
			resultStream.map(listToTabbedStringFn).forEach(line -> writeToFile(writer, line));
		}
	}

	private static void writeToFile(BufferedWriter writer, String line) {
		try {
			writer.write(String.format("%s%n", line));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
