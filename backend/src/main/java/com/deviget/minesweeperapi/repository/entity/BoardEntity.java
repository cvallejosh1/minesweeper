package com.deviget.minesweeperapi.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "board")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int cols;
    private int rows;
    private int mines;
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CellEntity> cells;
    @OneToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private GameEntity game;

    public BoardEntity(int cols, int rows, int mines) {
        this.cols = cols;
        this.rows = rows;
        this.mines = mines;
    }
}
