package com.deviget.minesweeperapi.domain.service;

import com.deviget.minesweeperapi.domain.model.Board;
import com.deviget.minesweeperapi.domain.model.Cell;
import com.deviget.minesweeperapi.domain.model.Game;
import com.deviget.minesweeperapi.exceptions.BoardNotFoundException;
import com.deviget.minesweeperapi.exceptions.CellNotFoundException;
import com.deviget.minesweeperapi.exceptions.GameAlreadyFinishedException;
import com.deviget.minesweeperapi.exceptions.GameNotActiveException;
import com.deviget.minesweeperapi.exceptions.GameNotFoundException;
import com.deviget.minesweeperapi.repository.GameRepository;
import com.deviget.minesweeperapi.repository.entity.BoardEntity;
import com.deviget.minesweeperapi.repository.entity.CellEntity;
import com.deviget.minesweeperapi.repository.entity.GameEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.deviget.minesweeperapi.domain.model.GameStatus.CREATED;
import static com.deviget.minesweeperapi.domain.model.GameStatus.FINISHED_LOST;
import static com.deviget.minesweeperapi.domain.model.GameStatus.FINISHED_WON;
import static com.deviget.minesweeperapi.domain.model.GameStatus.PAUSED;
import static com.deviget.minesweeperapi.domain.model.GameStatus.PLAYING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {


    @Mock
    private GameRepository gameRepository;
    @Mock
    private BoardService boardService;

    @InjectMocks
    private GameService gameService = new GameServiceImpl(boardService, gameRepository);

    private static final String USER_ID = "user_test";

    @Test
    @DisplayName("Service - Create a game successfully")
    void testCreateGameSuccessfully() {
        int cols = 3, rows = 3, mines = 2;
        List<Cell> cells = new ArrayList<>();
        for (int i=0; i< rows; i++) {
            for (int j=0; j<rows; j++) {
                cells.add(new Cell());
            }
        }
        Mockito.when(boardService.initCells(any(Board.class)))
                .thenReturn(cells);
        Game game = gameService.createGame(USER_ID, cols, rows, mines);
        Mockito.verify(gameRepository).save(any(GameEntity.class));

        assertThat(game).isNotNull();
        assertThat(game.getStatus()).isEqualTo(CREATED);
        assertThat(game.getBoard()).isNotNull();
        assertThat(game.getBoard().getCols()).isEqualTo(cols);
        assertThat(game.getBoard().getRows()).isEqualTo(rows);
        assertThat(game.getBoard().getMines()).isEqualTo(mines);
        assertThat(game.getBoard().getCells()).hasSize(9);
    }

    @Test
    @DisplayName("Service - Get games by user id")
    void testGetGamesByUser() {
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        List<GameEntity> gameEntityList = List.of(gameEntity);

        Mockito.when(gameRepository.findByUserId(eq(USER_ID)))
                .thenReturn(gameEntityList);
        List<Game> games = gameService.getGamesByUser(USER_ID);

        Mockito.verify(gameRepository).findByUserId(eq(USER_ID));

        assertThat(games).isNotNull();
        assertThat(games).hasSize(gameEntityList.size());
    }

    @Test
    @DisplayName("Service - Get game by id")
    void testGetGameById() {
        int gameId = 1;
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));
        Game game = gameService.getGameById(gameId);

        Mockito.verify(gameRepository).findById(eq(gameId));

        assertThat(game).isNotNull();
        assertThat(game.getId()).isEqualTo(gameId);
    }

    @Test
    @DisplayName("Service - Get game by invalid id")
    void testGetGameByInvalidId() {
        int gameId = 2;
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.empty());

        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> gameService.getGameById(gameId));
        Mockito.verify(gameRepository).findById(eq(gameId));
        assertThat(exception.getMessage()).isEqualTo("Game with id 2 not found");
    }

    @Test
    @DisplayName("Service - Pause a game given the game id")
    void testPauseGame() {
        int gameId = 1;
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        boolean result = gameService.pauseGame(gameId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Service - Pause a game given an invalid game id returns an exception")
    void testPauseGameWhenInvalidGameId() {
        int gameId = 2;

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.empty());

        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> gameService.pauseGame(gameId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0))
                .save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 2 not found");
    }

    @Test
    @DisplayName("Service - Pause a game when it already finished and lost return an exception")
    void testPauseGameWhenGameIsAlreadyFinishedLost() {
        int gameId = 2;
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(FINISHED_LOST.name()).build();

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        GameAlreadyFinishedException exception = assertThrows(GameAlreadyFinishedException.class,
                () -> gameService.pauseGame(gameId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0))
                .save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 2 is already finished");
    }

    @Test
    @DisplayName("Service - Pause a game when it already finished and won returns an exception")
    void testPauseGameWhenGameIsAlreadyFinishedWon() {
        int gameId = 2;
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(FINISHED_WON.name()).build();

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        GameAlreadyFinishedException exception = assertThrows(GameAlreadyFinishedException.class,
                () -> gameService.pauseGame(gameId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0))
                .save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 2 is already finished");
    }

    @Test
    @DisplayName("Service - Play a game given the game id")
    void testPlayGame() {
        int gameId = 1;
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(CREATED.name()).build();

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        boolean result = gameService.playGame(gameId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Service - Play a game given an invalid game id returns an exception")
    void testPlayGameWhenInvalidGameId() {
        int gameId = 2;

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.empty());

        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> gameService.playGame(gameId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0))
                .save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 2 not found");
    }

    @Test
    @DisplayName("Service - Play a game when it already finished and lost returns an exception")
    void testPlayGameWhenGameIsAlreadyFinishedLost() {
        int gameId = 2;
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(FINISHED_LOST.name()).build();

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        GameAlreadyFinishedException exception = assertThrows(GameAlreadyFinishedException.class,
                () -> gameService.playGame(gameId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0))
                .save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 2 is already finished");
    }

    @Test
    @DisplayName("Service - Play a game when it already finished and won returns an exception")
    void testPlayGameWhenGameIsAlreadyFinishedWon() {
        int gameId = 2;
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(new CellEntity())).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(FINISHED_WON.name()).build();

        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        GameAlreadyFinishedException exception = assertThrows(GameAlreadyFinishedException.class,
                () -> gameService.playGame(gameId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0))
                .save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 2 is already finished");
    }

    @Test
    @DisplayName("Service - Flag a cell when the game is on PLAYING status")
    void testFlagCell() {
        int gameId = 1, cellId = 10;
        CellEntity cellEntity = CellEntity.builder().id(cellId).build();
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(cellEntity)).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        boolean result = gameService.flagCell(gameId, cellId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Service - Flag a cell when the game id is invalid returns an exception")
    void testFlagCellWhenGameIdIsInvalid() {
        int gameId = 1, cellId = 10;
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.empty());

        GameNotFoundException exception = assertThrows(GameNotFoundException.class,
                () -> gameService.flagCell(gameId, cellId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0)).save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 1 not found");
    }

    @Test
    @DisplayName("Service - Flag a cell when the game is not on PLAYING status and returns an exception")
    void testFlagCellWhenGameIsNotOnPlayingStatus() {
        int gameId = 1, cellId = 10;
        CellEntity cellEntity = CellEntity.builder().id(cellId).build();
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(cellEntity)).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PAUSED.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        GameNotActiveException exception = assertThrows(GameNotActiveException.class,
                () -> gameService.flagCell(gameId, cellId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0)).save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 1 is not active");
    }

    @Test
    @DisplayName("Service - Flag a cell when the game does not have any board and returns an exception")
    void testFlagCellWhenGameDoesNotHaveAnyBoard() {
        int gameId = 1, cellId = 10;
        GameEntity gameEntity = GameEntity.builder().userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class,
                () -> gameService.flagCell(gameId, cellId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0)).save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Game with id 1 does not have any board associated");
    }

    @Test
    @DisplayName("Service - Flag a cell when the cell Id is invalid and returns an exception")
    void testFlagCellWhenCellIdToFlagIsInvalid() {
        int gameId = 1, cellId = 10;
        CellEntity cellEntity = CellEntity.builder().id(2).build();
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(cellEntity)).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        CellNotFoundException exception = assertThrows(CellNotFoundException.class,
                () -> gameService.flagCell(gameId, cellId));

        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0)).save(any(GameEntity.class));
        assertThat(exception.getMessage()).isEqualTo("Cell with id 10 and gameId 1 not found");
    }

    @Test
    @DisplayName("Service - Reveal a cell when the game is on PLAYING status and won")
    void testRevealCellAndFinishedWon() {
        int gameId = 1, cellId = 10;
        CellEntity cellEntity = CellEntity.builder().id(cellId)
                .minesAround(1)
                .build();
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(cellEntity)).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        Game game = gameService.revealCell(gameId, cellId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));

        assertThat(game).isNotNull();
        assertThat(game.getStatus()).isEqualTo(FINISHED_WON);
    }

    @Test
    @DisplayName("Service - Reveal a cell when the game is on PLAYING status and lost")
    void testRevealCellAndFinishedLost() {
        int gameId = 1, cellId = 10;
        CellEntity cellEntity = CellEntity.builder().id(cellId)
                .minesAround(1).mined(true)
                .build();
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(cellEntity)).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        Game game = gameService.revealCell(gameId, cellId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));

        assertThat(game).isNotNull();
        assertThat(game.getStatus()).isEqualTo(FINISHED_LOST);
    }

    @Test
    @DisplayName("Service - Reveal a cell when the game does not have any board associated")
    void testRevealCellWhenGameDoesNotHaveAnyBoard() {
        int gameId = 1, cellId = 10;
        GameEntity gameEntity = GameEntity.builder().userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        BoardNotFoundException exception = assertThrows(BoardNotFoundException.class,
                () -> gameService.revealCell(gameId, cellId));
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository, times(0)).save(any(GameEntity.class));

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Game with id 1 does not have any board associated");
    }

    @Test
    @DisplayName("Service - Reveal a cell and the game is still on PLAYING status")
    void testRevealCellAndStillPlaying() {
        int gameId = 1, cellId = 10;
        CellEntity cell1 = CellEntity.builder().id(cellId)
                .minesAround(1)
                .build();
        CellEntity cell2 = CellEntity.builder().id(cellId + 1)
                .minesAround(1)
                .build();
        BoardEntity boardEntity = BoardEntity.builder().cols(3)
                .rows(3).mines(2)
                .cells(List.of(cell1, cell2)).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(1).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        Game game = gameService.revealCell(gameId, cellId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));

        assertThat(game).isNotNull();
        assertThat(game.getStatus()).isEqualTo(PLAYING);
    }

    @Test
    @DisplayName("Service - Reveal a cell and the game is still on PLAYING status")
    void testRevealCellWhenCellDoesNotHaveMinesAround() throws Exception {
        int gameId = 22, cellId = 1321;
        gameService = new GameServiceImpl(new BoardServiceImpl(), gameRepository);
        ObjectMapper mapper = new ObjectMapper();
        Game gameData = mapper.readValue(new File("src/test/resources/testData/gameData.json"), Game.class);
        List<CellEntity> cellList = toListOfCellEntity(gameData.getBoard().getCells());
        BoardEntity boardEntity = BoardEntity.builder().cols(4)
                .rows(4).mines(2)
                .cells(cellList).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(gameId).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        Game game = gameService.revealCell(gameId, cellId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));

        assertThat(game).isNotNull();
        assertThat(game.getStatus()).isEqualTo(PLAYING);
        assertThat(game.getBoard()).isNotNull();
        assertThat(game.getBoard().getCells()).hasSize(cellList.size());

        Cell cellRevealed = game.getBoard().getCells().stream()
                .filter(c -> c.getId() == cellId).findFirst().get();
        assertThat(cellRevealed.isRevealed()).isTrue();
    }

    @Test
    @DisplayName("Service - Reveal a cell when at least one of the adjacent cell is revealed.")
    void testRevealCellWhenAdjacentCellRevealed() throws Exception {
        int gameId = 22, cellId = 1321;
        gameService = new GameServiceImpl(new BoardServiceImpl(), gameRepository);
        ObjectMapper mapper = new ObjectMapper();
        Game gameData = mapper.readValue(new File("src/test/resources/testData/gameData.json"), Game.class);
        gameData.getBoard().getCells().get(10).setRevealed(true);
        List<CellEntity> cellList = toListOfCellEntity(gameData.getBoard().getCells());
        BoardEntity boardEntity = BoardEntity.builder().cols(4)
                .rows(4).mines(2)
                .cells(cellList).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(gameId).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        Game game = gameService.revealCell(gameId, cellId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));

        assertThat(game).isNotNull();
        assertThat(game.getStatus()).isEqualTo(PLAYING);
        assertThat(game.getBoard()).isNotNull();
        assertThat(game.getBoard().getCells()).hasSize(cellList.size());

        Cell cellRevealed = game.getBoard().getCells().stream()
                .filter(c -> c.getId() == cellId).findFirst().get();
        assertThat(cellRevealed.isRevealed()).isTrue();
    }

    @Test
    @DisplayName("Service - Reveal a cell when at least one of the adjacent cell is mined.")
    void testRevealCellWhenAdjacentCellMined() throws Exception {
        int gameId = 22, cellId = 1321;
        gameService = new GameServiceImpl(new BoardServiceImpl(), gameRepository);
        ObjectMapper mapper = new ObjectMapper();
        Game gameData = mapper.readValue(new File("src/test/resources/testData/gameData.json"), Game.class);
        gameData.getBoard().getCells().get(10).setMined(true);
        List<CellEntity> cellList = toListOfCellEntity(gameData.getBoard().getCells());
        BoardEntity boardEntity = BoardEntity.builder().cols(4)
                .rows(4).mines(2)
                .cells(cellList).build();
        GameEntity gameEntity = GameEntity.builder()
                .board(boardEntity).userId(USER_ID)
                .id(gameId).status(PLAYING.name()).build();
        Mockito.when(gameRepository.findById(eq(gameId)))
                .thenReturn(Optional.of(gameEntity));

        Game game = gameService.revealCell(gameId, cellId);
        Mockito.verify(gameRepository).findById(eq(gameId));
        Mockito.verify(gameRepository).save(any(GameEntity.class));

        assertThat(game).isNotNull();
        assertThat(game.getStatus()).isEqualTo(PLAYING);
        assertThat(game.getBoard()).isNotNull();
        assertThat(game.getBoard().getCells()).hasSize(cellList.size());

        Cell cellRevealed = game.getBoard().getCells().stream()
                .filter(c -> c.getId() == cellId).findFirst().get();
        assertThat(cellRevealed.isRevealed()).isTrue();
    }

    private static List<CellEntity> toListOfCellEntity(List<Cell> cellList) {
        return cellList.stream()
                .map(c -> toCellEntity(c))
                .collect(Collectors.toList());
    }

    private static CellEntity toCellEntity(Cell cell) {
        return CellEntity.builder().id(cell.getId())
                .colIndex(cell.getY())
                .rowIndex(cell.getX())
                .mined(cell.isMined())
                .flagged(cell.isFlagged())
                .revealed(cell.isRevealed())
                .minesAround(cell.getMinesAround()).build();
    }

}
