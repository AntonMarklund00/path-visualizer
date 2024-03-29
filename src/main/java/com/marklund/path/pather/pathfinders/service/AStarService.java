package com.marklund.path.pather.pathfinders.service;

import com.marklund.path.pather.pathfinders.due.Cell;
import org.springframework.stereotype.Service;

@Service
public class AStarService <T extends Cell> extends DjikstraService<T> {

    @Override
    protected double getDistance(T current, T end){
        return Math.abs(current.getX() - end.getX()) + Math.abs(current.getY() - end.getY());
    }
}
