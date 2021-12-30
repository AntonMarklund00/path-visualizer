package com.marklund.path.pather.pathfinders.controller;

import com.marklund.path.pather.pathfinders.service.AStarService;
import com.marklund.path.pather.pathfinders.service.DjikstraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PathFindingController {

    @Autowired
    private AStarService aStarService;

    @Autowired
    private DjikstraService djikstraService;

    @GetMapping("/astar")
    public Map<Integer, List<Integer[]>> getAStarPath(
            @RequestParam(value = "grid") Integer[][] grid,
            @RequestParam(value = "starty") Integer startY,
            @RequestParam(value = "startx") Integer startX,
            @RequestParam(value = "endy") Integer endY,
            @RequestParam(value = "endx") Integer endX) {

        return aStarService.findClosestPath(grid, startY, startX, endY, endX);
    }

    @GetMapping("/djikstra")
    public Map<Integer, List<Integer[]>> getDjikstraPth(
            @RequestParam(value = "grid") Integer[][] grid,
            @RequestParam(value = "starty") Integer startY,
            @RequestParam(value = "startx") Integer startX,
            @RequestParam(value = "endy") Integer endY,
            @RequestParam(value = "endx") Integer endX) {

        return djikstraService.findClosestPath(grid, startY, startX, endY, endX);
    }
}
