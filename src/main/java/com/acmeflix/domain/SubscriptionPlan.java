package com.acmeflix.domain;

public enum SubscriptionPlan {
    BASIC(1), STANDARD(2), PREMIUM(3);
    private final short plan;

    SubscriptionPlan(int plan) {
        this.plan = (short) plan;
    }

    public short getPlan() {
        return this.plan;
    }

}
