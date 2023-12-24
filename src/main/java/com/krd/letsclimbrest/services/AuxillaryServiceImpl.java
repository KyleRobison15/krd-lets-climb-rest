package com.krd.letsclimbrest.services;

import com.krd.letsclimbrest.entities.BoulderGrade;
import com.krd.letsclimbrest.entities.Danger;
import com.krd.letsclimbrest.entities.Grade;
import com.krd.letsclimbrest.entities.Style;
import com.krd.letsclimbrest.repositories.BoulderGradeRepository;
import com.krd.letsclimbrest.repositories.DangerRepository;
import com.krd.letsclimbrest.repositories.GradeRepository;
import com.krd.letsclimbrest.repositories.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuxillaryServiceImpl implements AuxillaryService {

    @Autowired
    GradeRepository gradeRepo;

    @Autowired
    BoulderGradeRepository boulderGradeRepo;

    @Autowired
    StyleRepository styleRepo;

    @Autowired
    DangerRepository dangerRepo;


    @Override
    public List<Grade> getAllGrades() {
        return gradeRepo.findAllByOrderByIdAsc();
    }

    @Override
    public List<BoulderGrade> getAllBoulderGrades() {
        return boulderGradeRepo.findAllByOrderByIdAsc();
    }

    @Override
    public List<Style> getAllStyles() {
        return styleRepo.findAllByOrderByIdAsc();
    }

    @Override
    public List<Danger> getAllDangers() {
        return dangerRepo.findAllByOrderByIdAsc();
    }

    @Override
    public Map<String, Integer> getGradeMap() {
        List<Grade> gradeList = gradeRepo.findAllByOrderByIdAsc();
        Map<String, Integer> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getName, Grade::getId));
        return gradeMap;
    }
}
