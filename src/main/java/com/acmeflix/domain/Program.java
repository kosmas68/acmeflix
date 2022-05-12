package com.acmeflix.domain;

import lombok.Data;

@Data

public class Program {
    private int id;
    private String title;
    private ProgramType programType;
    private ProgramCategory programCategory;
}
