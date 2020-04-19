package com.ericfjosne.japanese;

import com.ericfjosne.japanese.model.VocabularyItem;
import com.ericfjosne.japanese.writers.ExcelVocabularyWriter;
import com.ericfjosne.japanese.writers.IVocabularyWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class FixVocabulary {

	private final String inFile;
	private final IVocabularyWriter writer;

	public static void main(String[] args) throws IOException {
		new FixVocabulary(
				"/Users/efj/Desktop/Topic2.txt",
				new ExcelVocabularyWriter("/Users/efj/Desktop/Topic2_final.xlsx","Topic 7")
		).doIt();
	}

	public FixVocabulary(String inFile, IVocabularyWriter writer){
		this.inFile = inFile;
		this.writer = writer;
	}

	public List<String> readAllLinesFromInFile() throws IOException {
		return Files.readAllLines(Paths.get(inFile), Charset.forName("UTF-8"));
	}

	public String cleanLine(String line){
		// Trim the line
		line = line.trim();

		// Remove comments from vocabulary sheet
		while(line.contains(" (*")){
			int index = line.indexOf(" (*");
			line = line.substring(0, index) + line.substring(index+5);
		}

		// Remove all double spaces
		while(line.indexOf("  ")!=-1) {
			line = line.replaceAll("  ", " ");
		}

		// Remove intonation markers
		line = line.replaceAll("┐", "");
		line = line.replaceAll("-", "");
		return line;
	}

	public void doIt() throws IOException {
		// Retrieve vocabulary file content
		List<String> lines = readAllLinesFromInFile();

		List<VocabularyItem> items = new ArrayList<>();
		for(int i=0; i<lines.size(); i++) {
			try {
				VocabularyItem item = getVocabularyItem(lines.get(i));
				// Fail if english has japanese character
				if(!item.isValid()){
					System.out.println("Line " + (i+1) + ": " + lines.get(i));
					System.out.println(" -> Kanji: " + item.getKanji());
					System.out.println(" -> Kanas: " + item.getKana());
					System.out.println(" -> English: " + item.getEnglish());
					throw new IllegalArgumentException("English language should not contain any japanese characters");
				}

				if(!items.contains(item)){
					items.add(item);
				}
			}
			catch(StringIndexOutOfBoundsException e){
				System.out.println("Line " + (i+1) + ": " + lines.get(i));
				throw new IllegalArgumentException("Unable to get vocabulary item from line");
			}
		}

		writer.write(items);
	}

	private VocabularyItem getVocabularyItem(String line) {
		// Get cleaned line
		line = cleanLine(line);

		// Assume kanji before first space and kanas before second space
		int firstSpace = line.indexOf(" ");
		int secondSpace = line.indexOf(" ", firstSpace+1);

		// normalize japanese, retrieve english
		VocabularyItem item = new VocabularyItem();
		item.setKanji(normalize(line.substring(0, firstSpace)));
		item.setKana(normalize(line.substring(firstSpace+1, secondSpace)));
		item.setEnglish(line.substring(secondSpace+1));

		return item;
	}

	private static String normalize(String s){
		// In case (han)dakuten characters have been decomposed, recompose them into a single one with Normalizer
		return Normalizer.normalize(s, Normalizer.Form.NFC);
	}
}
