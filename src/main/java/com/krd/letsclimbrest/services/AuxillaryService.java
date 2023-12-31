package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.BoulderGrade;
import com.krd.letsclimbrest.entities.Danger;
import com.krd.letsclimbrest.entities.Grade;
import com.krd.letsclimbrest.entities.Style;

import java.util.List;
import java.util.Map;

public interface AuxillaryService {

    List<Grade> getAllGrades();
    List<BoulderGrade> getAllBoulderGrades();
    List<Style> getAllStyles();
    List<Danger> getAllDangers();
    Map<String, Integer> getGradeMap();

}
