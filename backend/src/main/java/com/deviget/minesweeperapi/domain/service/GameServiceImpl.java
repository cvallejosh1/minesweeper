package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Board;
import com.deviget.minesweeperapi.domain.model.Cell;
import com.deviget.minesweeperapi.domain.model.Game;
import com.deviget.minesweeperapi.domain.model.GameStatus;
import com.deviget.minesweeperapi.repository.GameRepository;
import com.deviget.minesweeperapi.repository.entity.BoardEntity;
import com.deviget.minesweeperapi.repository.entity.CellEntity;
import com.deviget.minesweeperapi.repository.entity.GameEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

    private BoardService boardService;
    private GameRepository gameRepository;

    public GameServiceImpl(BoardService boardService, GameRepository gameRepository) {
        this.boardService = boardService;
        this.gameRepository = gameRepository;
    }

    @Override
    public Game createGame(int cols, int rows, int mines) {
        Board board = Board.builder().cols(cols).rows(rows).mines(mines).build();
        List<Cell> cells = this.boardService.initCells(board);

        BoardEntity boardEntity = new BoardEntity(cols, rows, mines);
        boardEntity.setCells(toListOfCellEntity(cells, boardEntity));
        GameEntity game = GameEntity.builder()
                .board(boardEntity)
                .status(GameStatus.CREATED.name()).build();
        boardEntity.setGame(game);
        this.gameRepository.save(game);
        return toGame(game);
    }

    private static Game toGame(GameEntity gameEntity) {
        return Game.builder().id(gameEntity.getId())
                .board(toBoard(gameEntity.getBoard()))
                .status(GameStatus.valueOf(gameEntity.getStatus())).build();
    }

    private static Board toBoard(BoardEntity boardEntity) {
        return Board.builder()
                .cols(boardEntity.getCols())
                .rows(boardEntity.getRows())
                .mines(boardEntity.getMines())
                .cells(toListOfCell(boardEntity.getCells())).build();
    }

    private static List<Cell> toListOfCell(List<CellEntity> cellEntityList) {
        return cellEntityList.stream()
                .map(GameServiceImpl::toCell)
                .collect(Collectors.toList());
    }

    private static Cell toCell(CellEntity cellEntity) {
        return Cell.builder().y(cellEntity.getColIndex())
                .x(cellEntity.getRowIndex())
                .mined(cellEntity.isMined())
                .flagged(cellEntity.isFlagged())
                .revealed(cellEntity.isRevealed())
                .minesAround(cellEntity.getMinesAround()).build();
    }

    private static List<CellEntity> toListOfCellEntity(List<Cell> cellList, BoardEntity boardEntity) {
        return cellList.stream()
                .map(c -> toCellEntity(c, boardEntity))
                .collect(Collectors.toList());
    }

    private static CellEntity toCellEntity(Cell cell, BoardEntity boardEntity) {
        return CellEntity.builder().colIndex(cell.getY())
                .rowIndex(cell.getX())
                .mined(cell.isMined())
                .flagged(cell.isFlagged())
                .revealed(cell.isRevealed())
                .minesAround(cell.getMinesAround())
                .board(boardEntity).build();
    }


}
