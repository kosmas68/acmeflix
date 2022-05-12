package com.acmeflix.domain;

public enum ProgramType {
    SERIES(1), MOVIE(2);
    private final short type;

    ProgramType(int type) {
        this.type = (short) type;
    }

    public short getType() {
        return this.type;
    }

}
