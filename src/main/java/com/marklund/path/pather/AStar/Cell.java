package com.marklund.path.pather.AStar;

public abstract class Cell {

    private int x;
    private int y;
    protected int distanceFromStart;
    protected double heuristicDistance;
    protected Cell parent;
    protected boolean isVisited;
    protected boolean isWall;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDistanceFromStart() {
        return this.distanceFromStart;
    }

    public double getHeuristicDistance() {
        return this.heuristicDistance;
    }

    public Cell getParent() {
        return parent;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public void setHeuristicDistance(double heuristicDistance) {
        this.heuristicDistance = heuristicDistance;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public double getCombinedDistance() {
        return getHeuristicDistance() + getDistanceFromStart();
    }
}
