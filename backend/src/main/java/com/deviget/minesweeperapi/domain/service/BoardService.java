package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Board;
import com.deviget.minesweeperapi.domain.model.Cell;

import java.util.List;

public interface BoardService {

    List<Cell> initCells(Board board);
}
