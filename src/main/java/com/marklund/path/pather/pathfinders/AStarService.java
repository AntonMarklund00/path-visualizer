package com.marklund.path.pather.pathfinders;

import com.marklund.path.pather.utils.MinHeap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class AStarService <T extends Cell>{

    private T[][] vertexMatrix;
    private T goal;
    private MinHeap<T> priorityQueue;
    private List<Integer[]> cellsVisited;


    public Map<Integer, List<Integer[]>> findClosestPath(Integer[][] matrix, int startY, int startX, int endY, int endX) {
        vertexMatrix = getVertexMatrix(matrix);
        priorityQueue = new MinHeap<T>(vertexMatrix.length * vertexMatrix[0].length);
        cellsVisited = new ArrayList<>();

        T start = vertexMatrix[startY][startX];
        goal = vertexMatrix[endY][endX];

        start.setDistanceFromStart(0);
        start.setHeuristicDistance(getDistance.apply(start, goal));

        priorityQueue.insert(start.getCombinedDistance(), start);

        findRouteFromStartToFinnish();

        Map<Integer, List<Integer[]>> map = new HashMap<>();
        map.put(0, cellsVisited);
        map.put(1, getShortestPathByThePathsVisited());

        return map;
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
            List<T> neighbours = getNeighbours(current);
            for (T neighbour : neighbours) {
                int costFromStart = current.getDistanceFromStart() + 1;
                if( neighbour != null && !neighbour.isWall() && (!neighbour.isVisited() || costFromStart < neighbour.getDistanceFromStart())) {
                    double heuristicDistance = getDistance.apply(neighbour, goal);
                    neighbour.setVisited(true);
                    neighbour.setDistanceFromStart(costFromStart);
                    neighbour.setHeuristicDistance(heuristicDistance);
                    neighbour.setParent(current);
                    if(neighbour.getX() == goal.getX() && neighbour.getY() == goal.getY())
                        return;

                    addToQueueAndVisitedList.accept(neighbour);
                }
            }
        }
    }

    private final Consumer<T> addToQueueAndVisitedList = neighbour -> {
        priorityQueue.insert(neighbour.getCombinedDistance(), neighbour);
        cellsVisited.add(new Integer[]{neighbour.getX(), neighbour.getY()});
    };

    private List<Integer[]> getShortestPathByThePathsVisited() {
        List<Integer[]> shortestPath = new java.util.ArrayList<>();
        if (goal.getParent() == null) {
            return shortestPath;
        }
        T current = vertexMatrix[goal.getParent().getX()][goal.getParent().getY()];
        while (current.getParent() != null) {
            shortestPath.add(new Integer[]{current.getX(), current.getY()});
            current = (T) current.getParent();
        }

        return shortestPath;
    }

    private BiFunction<T, T, Integer> getDistance = (current, end)
            -> Math.abs(current.getX() - end.getX()) + Math.abs(current.getY() - end.getY());


    public List<T> getNeighbours(T vertex) {
        List<T> neighbours = new java.util.ArrayList<>();
        int startPosX = (vertex.getX() - 1 < 0) ? vertex.getX() : vertex.getX()-1;
        int startPosY = (vertex.getY() - 1 < 0) ? vertex.getY() : vertex.getY()-1;
        int endPosX =   (vertex.getX() + 1 > getListXSize.get()) ? vertex.getX() : vertex.getX()+1;
        int endPosY =   (vertex.getY() + 1 > getListXSize.get()) ? vertex.getY() : vertex.getY()+1;

        for (int rowNum=startPosY; rowNum<=endPosY; rowNum++) {
            for (int colNum=startPosX; colNum<=endPosX; colNum++) {
                if(rowNum == vertex.getY() && colNum == vertex.getX()) continue;
                neighbours.add(vertexMatrix[colNum][rowNum]);
            }
        }
        return neighbours;
    }

    private Supplier<Integer> getListXSize = () -> vertexMatrix[0].length;
}
