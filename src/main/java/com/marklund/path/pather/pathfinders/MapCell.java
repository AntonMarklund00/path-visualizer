package com.marklund.path.pather.pathfinders;

public class MapCell extends Cell{
    public MapCell(int x, int y) {
        super(x, y);
        distanceFromStart = 0;
        heuristicDistance = 0;
        parent = null;
        isVisited = false;
        isWall = false;
    }
}
