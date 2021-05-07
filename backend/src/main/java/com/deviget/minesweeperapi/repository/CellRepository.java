package com.deviget.minesweeperapi.repository;

import com.deviget.minesweeperapi.repository.entity.CellEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CellRepository extends JpaRepository<CellEntity, Integer>  {
}
