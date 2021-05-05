package com.deviget.minesweeperapi.controller.response;

import com.deviget.minesweeperapi.domain.model.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private int id;
    private int rows;
    private int cols;
    private int mines;
    private String status;

    public static GameResponse build(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .rows(game.getBoard().getRows())
                .cols(game.getBoard().getCols())
                .mines(game.getBoard().getMines())
                .status(game.getStatus().name()).build();
    }
}
