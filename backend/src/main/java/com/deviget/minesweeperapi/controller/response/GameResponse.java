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

    private Game game;
}
