package com.acmeflix.domain;

import lombok.Data;

import java.util.List;

@Data
public class Series extends Program {
    private List<Season> seasons;
}
