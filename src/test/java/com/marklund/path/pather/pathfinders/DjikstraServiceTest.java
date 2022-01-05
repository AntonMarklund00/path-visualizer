package com.marklund.path.pather.pathfinders;

import com.marklund.path.pather.pathfinders.due.Cell;
import com.marklund.path.pather.pathfinders.service.DjikstraService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class DjikstraServiceTest {
    @Autowired
    private DjikstraService<Cell> djikstraService;

    private Integer[][] underTest = new Integer[][] {
            {1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,1},
            {1,2,0,0,0,0,0,3,1},
            {1,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1},
    };

    @Test
    public void isValidIfTheAmountOfVisitedNodesAreCorrect() {
        // when
        Map<Integer, List<Integer[]>> givenResultFromService =
                djikstraService.findClosestPath(underTest, 3, 1, 3, 7);
        int amountOfStepsTaken = givenResultFromService.get(0).size();

        // then
        assertThat(amountOfStepsTaken).isEqualTo(22);
    }

    @Test
    public void isValidIfTheAmountOfStepsInShortestPathIsCorrect(){
        // when
        Map<Integer, List<Integer[]>> givenResultFromService =
                djikstraService.findClosestPath(underTest, 3, 1, 3, 7);
        int amountOfStepsTaken = givenResultFromService.get(1).size();

        // then
        assertThat(amountOfStepsTaken).isEqualTo(5);
    }
}
