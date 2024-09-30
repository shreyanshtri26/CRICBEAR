package com.example.tournament.model.DTO;

public class MatchSemiDTOConverter {

    public static MatchSemiDTO convert(Object[] row) {
//        if (row == null || row.length < 6) {
//            throw new IllegalArgumentException("Invalid data provided to convert.");
//        }
        Integer teamId = (Integer) row[0];
        Integer groupNumber = (Integer) row[1];
        Integer points = (Integer) row[2];
        Double nrr = (Double) row[3];

        return new MatchSemiDTO(teamId, groupNumber,points,nrr);
    }
}

