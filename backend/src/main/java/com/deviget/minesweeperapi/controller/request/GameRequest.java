package com.deviget.minesweeperapi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {

    private String userId;
    private int rows;
    private int cols;
    private int mines;
}
