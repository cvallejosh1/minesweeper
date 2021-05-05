package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Board;
import com.deviget.minesweeperapi.domain.model.Cell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {

    @Override
    public List<Cell> initCells(Board board) {
        int mines = board.getMines();
        int rows = board.getRows();
        int cols = board.getCols();

        Set<Integer> minesSet = new HashSet<>();
        while (minesSet.size() < mines) {
            minesSet.add((int) (Math.random() * ((rows * cols) - 1)));
        }


        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells.add(Cell.builder()
                        .x(i)
                        .y(j)
                        .mined(minesSet.contains((i*rows) + j))
                        .build());
            }
        }

        cells.stream().forEach(cell -> {
            long minesAround = getAdjacentCellsStream(cells, cell)
                    .filter(Cell::isMined).count();
            cell.setMinesAround((int) minesAround);
        });

        return cells;
    }

    private Stream<Cell> getAdjacentCellsStream(List<Cell> cells, Cell cell) {
        return cells.stream().filter(c -> c.isAroundTo(cell));
    }
}
