package com.acmeflix.domain;

public enum ProgramCategory {
    ACTION(1), COMEDY(2), DRAMA(3), FANTASY(4), HORROR(5), MYSTERY(6), ROMANCE(7), THRILLER(8), DOCUMENTARY(9);
    private final short category;

    ProgramCategory(int category) {
        this.category = (short) category;
    }

    public short getCategory() {
        return this.category;
    }

}
