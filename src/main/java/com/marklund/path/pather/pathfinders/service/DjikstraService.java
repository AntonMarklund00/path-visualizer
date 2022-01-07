package com.marklund.path.pather.pathfinders.service;

import com.marklund.path.pather.pathfinders.due.Cell;
import com.marklund.path.pather.pathfinders.due.MapCell;
import com.marklund.path.pather.utils.MinHeap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class DjikstraService <T extends Cell>{

    private T[][] vertexMatrix;
    private T goal;
    private T start;
    private MinHeap<T> priorityQueue;
    private List<Integer[]> cellsVisited;

    public Map<Integer, List<Integer[]>> findClosestPath(Integer[][] matrix, int startY, int startX, int endY, int endX) {
        vertexMatrix = getVertexMatrix(matrix);
        priorityQueue = new MinHeap<>(vertexMatrix.length * vertexMatrix[0].length);
        cellsVisited = new ArrayList<>();

        start = vertexMatrix[startY][startX];
        goal = vertexMatrix[endY][endX];

        start.setDistanceFromStart(0);
        start.setHeuristicDistance(getDistance(start, goal));

        priorityQueue.insert(start.getCombinedDistance(), start);

        start();

        Map<Integer, List<Integer[]>> map = new HashMap<>();
        map.put(0, cellsVisited);
        map.put(1, getShortestPathByThePathsVisited());

        return map;
    }

    protected void start(){
        findRouteFromStartToFinnish();
    }

    private T[][] getVertexMatrix(Integer[][] matrix) {
        T[][] vertexMatrix = (T[][]) new MapCell[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                T cell = (T) new MapCell(i, j);
                if (matrix[i][j] == 1){
                    cell.setWall(true);
                }
                vertexMatrix[i][j] = cell;
            }
        }
        return vertexMatrix;
    }

    private void findRouteFromStartToFinnish() {
        while (!priorityQueue.isEmpty()) {
            T current = priorityQueue.remove();
            current.setVisited(true);
            List<T> neighbours = getAllFourNeighbours(current);
            for (T neighbour : neighbours) {
                double costFromStart = current.getDistanceFromStart() + getAdditionalValueFromStart.get();
                double heuristicDistance = getDistance(neighbour, goal);
                if( neighbour != null && !neighbour.isWall() && (!neighbour.isVisited() || costFromStart < neighbour.getDistanceFromStart())) {
                    neighbour.setDistanceFromStart(costFromStart);
                    neighbour.setHeuristicDistance(heuristicDistance);
                    neighbour.setParent(current);
                    if(isEnd(neighbour)){
                        return;
                    }
                    addToQueueAndVisitedList(neighbour);
                    neighbour.setVisited(true);
                }
            }
        }
    }

    private boolean isEnd(T current) {
        return current.getY() == goal.getY() && current.getX() == goal.getX();
    }

    private void addToQueueAndVisitedList(T neighbour) {
        priorityQueue.insert(neighbour.getCombinedDistance(), neighbour);
        if (!neighbour.isVisited())
            cellsVisited.add(new Integer[]{neighbour.getY(), neighbour.getX()});
    }

    private List<Integer[]> getShortestPathByThePathsVisited() {
        List<Integer[]> shortestPath = new java.util.ArrayList<>();
        if (goal.getParent() == null) {
            return shortestPath;
        }
        T current = vertexMatrix[goal.getParent().getY()][goal.getParent().getX()];
        while (current.getParent() != null) {
            shortestPath.add(new Integer[]{current.getY(), current.getX()});
            current = (T) current.getParent();
        }

        return shortestPath;
    }

    protected double getDistance(T current, T end){
        return 0;
    }


    private List<T> getAllFourNeighbours(T vertex) {
        List<T> neighbours = new java.util.ArrayList<>();
        int y = vertex.getY();
        int x = vertex.getX();
        if(y + 1 < vertexMatrix[0].length){
            neighbours.add(vertexMatrix[y + 1][x]);
        }
        if(y - 1 >= 0){
            neighbours.add(vertexMatrix[y - 1][x]);
        }
        if(x + 1 < vertexMatrix[0].length){
            neighbours.add(vertexMatrix[y][x + 1]);
        }
        if(x - 1 >= 0){
            neighbours.add(vertexMatrix[y][x - 1]);
        }
        return neighbours;
    }

    private List<T> getAllEightNeighbours(T vertex) {
        List<T> neighbours = new java.util.ArrayList<>();
        int startPosX = (vertex.getY() - 1 < 0) ? vertex.getY() : vertex.getY() - 1;
        int startPosY = (vertex.getX() - 1 < 0) ? vertex.getX() : vertex.getX() - 1;
        int endPosX = (vertex.getY() + 1 > getListXSize.get()) ? vertex.getY() : vertex.getY() + 1;
        int endPosY = (vertex.getX() + 1 > getListXSize.get()) ? vertex.getX() : vertex.getX() + 1;

        for (int rowNum = startPosY; rowNum <= endPosY; rowNum++) {
            for (int colNum = startPosX; colNum <= endPosX; colNum++) {
                if (rowNum == vertex.getX() && colNum == vertex.getY()) continue;
                neighbours.add(vertexMatrix[colNum][rowNum]);
            }
        }
        return neighbours;
    }

    private final Supplier<Double> getAdditionalValueFromStart = () -> 1.0;
    private final Supplier<Integer> getListXSize = () -> vertexMatrix[0].length;
}
