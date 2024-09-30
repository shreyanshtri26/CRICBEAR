package com.example.tournament.model.DTO;

import java.util.Comparator;

public class MatchSemiDTOComparator implements Comparator<MatchSemiDTO> {

    @Override
    public int compare(MatchSemiDTO o1, MatchSemiDTO o2) {
        // First compare by points in descending order
        int pointsComparison = o2.getPoints().compareTo(o1.getPoints());
        if (pointsComparison != 0) {
            return pointsComparison;
        }

        // If points are the same, compare by net run rate in ascending order
        return o1.getNrr().compareTo(o2.getNrr());
    }
}
