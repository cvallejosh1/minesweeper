package com.deviget.minesweeperapi.domain.service;


import com.deviget.minesweeperapi.domain.model.Board;
import com.deviget.minesweeperapi.domain.model.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardServiceImplTest {

    private BoardService boardService = new BoardServiceImpl();

    @Test
    void testInitCells() {
        Board board = Board.builder().rows(4).cols(4).mines(2).build();
        List<Cell> cells = this.boardService.initCells(board);
        long minedCells = cells.stream().filter(Cell::isMined).count();

        assertThat(cells).hasSize(16);
        assertThat(minedCells).isEqualTo(2);
    }
}
