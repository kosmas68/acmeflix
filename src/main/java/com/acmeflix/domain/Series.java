package com.acmeflix.domain;

import lombok.Data;

import java.util.List;

@Data
//@SuperBuilder
public class Series extends Program {
    private List<Season> seasons;
}
