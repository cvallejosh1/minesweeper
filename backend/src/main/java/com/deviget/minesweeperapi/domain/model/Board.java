package com.deviget.minesweeperapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Domain class to represent cell object
 *
 * @author carlos.vallejos
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    private int rows;
    private int cols;
    private int mines;
    private List<Cell> cells;
}
