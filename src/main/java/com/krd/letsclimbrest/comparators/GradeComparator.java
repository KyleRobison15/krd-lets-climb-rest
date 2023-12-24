package com.krd.letsclimbrest.comparators;

import com.krd.letsclimbrest.entities.Climb;
import com.krd.letsclimbrest.services.AuxillaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.Map;

/**
 * Custom comparator for sorting by Yosemite Decimal System climbing grades
 */
@RequiredArgsConstructor
public class GradeComparator implements Comparator<Climb> {


    private final AuxillaryService auxSvc;
    private final Sort.Direction sortOrder;

    /**
     *
     * The sort order of each climb will be determined by looking up the grade's sort position in the GradeMap from our Grade table in the database
     *
     * @param climb1 the climb being compared to climb 2
     * @param climb2 the climb being compared to climb 1
     * @return the sort order
     */

    @Override
    public int compare(Climb climb1, Climb climb2) {

        // Flag for determining ASC or DESC sort order
        boolean isAscending = sortOrder.equals(Sort.Direction.ASC);

        // Get the GradeMap from our auxSvc
        Map<String, Integer> gradeSortOrder = auxSvc.getGradeMap();

        // Look up the sort position for climb1 grade in the GradeMap
        Integer order1 = gradeSortOrder.getOrDefault(climb1.getGrade(), Integer.MAX_VALUE);
        // Look up the sort position for climb2 grade in the GradeMap
        Integer order2 = gradeSortOrder.getOrDefault(climb2.getGrade(), Integer.MAX_VALUE);

        // Compare the two sort positions to determine sort order
        int result = Integer.compare(order1, order2);

        // Return the sort order based on desc or asc flag
        return isAscending ? result : -result;
    }
}
