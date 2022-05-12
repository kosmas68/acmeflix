package com.acmeflix.domain;

public enum Rate {
    ONE_STAR(1), TWO_STARS(2), THREE_STARS(3), FOUR_STARS(4), FIVE_STARS(5);
    private final short rate;

    Rate(int rate) {
        this.rate = (short) rate;
    }

    public short getRate() {
        return this.rate;
    }

}
