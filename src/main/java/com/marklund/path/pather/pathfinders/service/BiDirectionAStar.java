package com.marklund.path.pather.pathfinders.service;

import com.marklund.path.pather.pathfinders.due.Cell;
import org.springframework.stereotype.Service;


@Service
public class BiDirectionAStar <T extends Cell> extends DjikstraService<T>{

}
