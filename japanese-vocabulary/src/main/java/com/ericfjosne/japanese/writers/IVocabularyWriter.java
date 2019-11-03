package com.ericfjosne.japanese.writers;

import com.ericfjosne.japanese.model.VocabularyItem;

import java.io.IOException;
import java.util.List;

public interface IVocabularyWriter {

    public void write(List<VocabularyItem> items) throws IOException;
}
