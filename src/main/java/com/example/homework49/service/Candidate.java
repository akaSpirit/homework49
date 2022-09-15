package com.example.homework49.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Candidate {
    private Integer id;
    private String name;
    private String photo;
    private int vote;
    private double votePercent;
}
