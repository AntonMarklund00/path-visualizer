package com.marklund.path.pather.AStar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/astar")
public class AStarController {

    @Autowired
    private AStarService aStarService;

    @GetMapping
    public Map<Integer, List<Integer[]>> getAStarPath(
            @RequestParam(value = "grid") Integer[][] grid,
            @RequestParam(value = "starty") Integer startY,
            @RequestParam(value = "startx") Integer startX,
            @RequestParam(value = "endy") Integer endY,
            @RequestParam(value = "endx") Integer endX) {

        return aStarService.findClosestPath(grid, startY, startX, endY, endX);

    }
}
