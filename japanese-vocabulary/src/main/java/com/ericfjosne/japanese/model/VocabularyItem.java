package com.ericfjosne.japanese.model;

import java.util.Comparator;
import java.util.regex.Pattern;

public class VocabularyItem {
    private String kanji;
    private String kana;
    private String english;

    public boolean isValid(){
        Pattern pat = Pattern.compile(".*(\\p{InHiragana}|\\p{InKatakana}|\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}).*");
        return !pat.matcher(english).find();
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getKana() {
        return kana;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    @Override
    public boolean equals(Object o) {
        VocabularyItem item = (VocabularyItem) o;
        return getKanji().equals(item.getKanji());
    }
}
