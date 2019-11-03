package com.ericfjosne.japanese.writers;

import com.ericfjosne.japanese.model.VocabularyItem;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TsvVocabularyWriter implements IVocabularyWriter {

    private String outputFilePath;

    public TsvVocabularyWriter(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void write(List<VocabularyItem> items) throws IOException {
        try(OutputStream out = Files.newOutputStream(Paths.get(outputFilePath))) {
            for (VocabularyItem item : items) {
                StringBuffer newLine = new StringBuffer();
                newLine.append(item.getKanji());
                newLine.append("\t");
                newLine.append(item.getKana());
                newLine.append("\t");
                newLine.append(item.getEnglish());
                newLine.append("\n");
                out.write(newLine.toString().getBytes("UTF-8"));
            }
            out.flush();
        }
    }
}
