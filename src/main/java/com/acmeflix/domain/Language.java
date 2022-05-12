package com.acmeflix.domain;

public enum Language {
    CHINESE(1), SPANISH(2), ENGLISH(3), HINDI(4), BENGALI(5), PORTUGUESE(6), RUSSIAN(7), JAPANESE(8), PUNJABI(9), VIETNAMESE(10), MARATHI(11), TELUGU(12), TURKISH(13), KOREAN(14), FRENCH(15), GERMAN(16), TAMIL(17), URDU(18), JAVANESE(19), ITALIAN(20), ARABIC(21), GUJARATI(22), PERSIAN(23), BHOJPURI(24), MINNAN(25), HAKKA(26), HAUSA(27);
    private final short language;

    Language(int language) {
        this.language = (short) language;
    }

    public short getLanguage() {
        return this.language;
    }

}
