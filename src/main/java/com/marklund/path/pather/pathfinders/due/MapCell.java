package com.marklund.path.pather.pathfinders.due;

import com.marklund.path.pather.pathfinders.due.Cell;

public class MapCell extends Cell {
    public MapCell(int y, int x) {
        super(y, x);
        distanceFromStart = 0;
        heuristicDistance = 0;
        parent = null;
        isVisited = false;
        isWall = false;
    }
}
