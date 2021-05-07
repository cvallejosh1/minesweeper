package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Board;
import com.deviget.minesweeperapi.domain.model.Cell;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface BoardService {

    List<Cell> initCells(Board board);
    <T> Stream<T> getAdjacentCellsStreamGeneric(List<T> cells, Predicate<T> filter);
}
