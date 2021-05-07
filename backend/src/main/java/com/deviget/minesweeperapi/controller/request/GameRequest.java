package com.deviget.minesweeperapi.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {

    @Schema(example = "user_0001", description = "User id who is playing the game")
    private String userId;
    @Schema(defaultValue = "8", description = "Number of rows")
    private int rows;
    @Schema(defaultValue = "8", description = "Number of columns")
    private int cols;
    @Schema(defaultValue = "5", description = "Number of mines to be found")
    private int mines;
}
