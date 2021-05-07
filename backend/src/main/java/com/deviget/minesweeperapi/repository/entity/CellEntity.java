package com.deviget.minesweeperapi.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cell")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CellEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private int colIndex;
    private int rowIndex;
    private boolean mined;
    private int minesAround;
    private boolean revealed;
    private boolean flagged;
    @ManyToOne(fetch = FetchType.LAZY)
    private BoardEntity board;
}
