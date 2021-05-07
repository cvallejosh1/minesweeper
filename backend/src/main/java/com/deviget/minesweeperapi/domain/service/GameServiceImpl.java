package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Board;
import com.deviget.minesweeperapi.domain.model.Cell;
import com.deviget.minesweeperapi.domain.model.Game;
import com.deviget.minesweeperapi.domain.model.GameStatus;
import com.deviget.minesweeperapi.exceptions.CellNotFoundException;
import com.deviget.minesweeperapi.exceptions.GameAlreadyFinishedException;
import com.deviget.minesweeperapi.exceptions.GameNotActiveException;
import com.deviget.minesweeperapi.exceptions.GameNotFoundException;
import com.deviget.minesweeperapi.repository.CellRepository;
import com.deviget.minesweeperapi.repository.GameRepository;
import com.deviget.minesweeperapi.repository.entity.BoardEntity;
import com.deviget.minesweeperapi.repository.entity.CellEntity;
import com.deviget.minesweeperapi.repository.entity.GameEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
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
    public Game createGame(String userId, int cols, int rows, int mines) {
        Board board = Board.builder().cols(cols).rows(rows).mines(mines).build();
        List<Cell> cells = this.boardService.initCells(board);

        BoardEntity boardEntity = new BoardEntity(cols, rows, mines);
        boardEntity.setCells(toListOfCellEntity(cells, boardEntity));
        GameEntity game = GameEntity.builder()
                .board(boardEntity)
                .userId(userId)
                .status(GameStatus.CREATED.name()).build();
        boardEntity.setGame(game);
        this.gameRepository.save(game);
        return toGame(game);
    }

    @Override
    public List<Game> getGamesByUser(String userId) {
        List<GameEntity> gameEntityList = this.gameRepository.findByUserId(userId);
        return Optional.ofNullable(gameEntityList)
                .orElseGet(Collections::emptyList)
                .stream().filter(Objects::nonNull)
                .map(GameServiceImpl::toGame)
                .collect(Collectors.toList());
    }

    @Override
    public Game getGameById(int gameId) {
        GameEntity gameEntity = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        return toGame(gameEntity);
    }

    @Override
    public boolean pauseGame(int gameId) {
        GameEntity gameEntity = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        String gameStatus = gameEntity.getStatus();
        if (gameStatus.equals(GameStatus.FINISHED_LOST)
                || gameStatus.equals(GameStatus.FINISHED_WON) ) {
            throw new GameAlreadyFinishedException(gameId);
        }
        gameEntity.setStatus(GameStatus.PAUSED.name());
        this.gameRepository.save(gameEntity);
        return true;
    }

    @Override
    public boolean playGame(int gameId) {
        GameEntity gameEntity = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        String gameStatus = gameEntity.getStatus();
        if (gameStatus.equals(GameStatus.FINISHED_LOST)
                || gameStatus.equals(GameStatus.FINISHED_WON) ) {
            throw new GameAlreadyFinishedException(gameId);
        }
        gameEntity.setStatus(GameStatus.PLAYING.name());
        this.gameRepository.save(gameEntity);
        return true;
    }

    @Override
    public Game revealCell(int gameId, int cellId) {
        GameEntity gameEntity = getPlayingGameById(gameId);
        BoardEntity boardEntity = gameEntity.getBoard();
        if (boardEntity != null) {
            CellEntity cellEntity = filterCellsById(boardEntity.getCells(), gameId, cellId);
            revealCellsAndUpdateStatus(boardEntity.getCells(), gameEntity, cellEntity);
            gameRepository.save(gameEntity);
        }
        return toGame(gameEntity);
    }

    private void revealCellsAndUpdateStatus(List<CellEntity> cells, GameEntity gameEntity, CellEntity cellEntity) {
        if (cellEntity.isMined()) {
            gameEntity.setStatus(GameStatus.FINISHED_LOST.name());
            return;
        }

        if (cellEntity.getMinesAround() == 0) {
            revealAdjacentCells(cells, cellEntity);
        }
        cellEntity.setRevealed(true);

        long totalCellsRevealed = cells
                .stream().filter(CellEntity::isRevealed).count();
        long totalMines = cells
                .stream().filter(CellEntity::isMined).count();

        if (totalCellsRevealed + totalMines == cells.size()) {
            gameEntity.setStatus(GameStatus.FINISHED_WON.name());
        }
    }

    private void revealAdjacentCells(List<CellEntity> cells, CellEntity cell) {
        boardService.getAdjacentCellsStreamGeneric(cells, isAroundTo(cell))
                .filter(c -> !c.isRevealed() && !c.isMined())
                .forEach(adjacentCell -> {
                    adjacentCell.setRevealed(true);
                    if (adjacentCell.getMinesAround() == 0) {
                        revealAdjacentCells(cells, cell);
                    }
                });
    }

    private Predicate<CellEntity> isAroundTo(CellEntity cell) {
        return c -> Math.abs(c.getRowIndex() - cell.getRowIndex()) <= 1 &&
                Math.abs(c.getColIndex() - cell.getColIndex()) <= 1 &&
                !(c.getRowIndex() == cell.getRowIndex() && c.getColIndex() == cell.getColIndex());
    }

    @Override
    public boolean flagCell(int gameId, int cellId) {
        GameEntity gameEntity = getPlayingGameById(gameId);
        BoardEntity boardEntity = gameEntity.getBoard();
        if (boardEntity != null) {
            CellEntity cellEntity = filterCellsById(boardEntity.getCells(), gameId, cellId);
            cellEntity.setFlagged(true);
            gameRepository.save(gameEntity);
        }
        return true;
    }

    private GameEntity getPlayingGameById(int gameId) {
        GameEntity gameEntity = this.gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        String gameStatus = gameEntity.getStatus();
        if (!gameStatus.equals(GameStatus.PLAYING.name())) {
            throw new GameNotActiveException(gameId);
        }
        return gameEntity;
    }

    private CellEntity filterCellsById(List<CellEntity> cells, int gameId, int cellId) {
        return Optional.ofNullable(cells)
                .orElseGet(Collections::emptyList)
                .stream().filter(Objects::nonNull)
                .filter(cell -> cell.getId() == cellId)
                .findFirst().orElseThrow(() -> new CellNotFoundException(gameId, cellId));
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
        return Cell.builder().id(cellEntity.getId())
                .y(cellEntity.getColIndex())
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
