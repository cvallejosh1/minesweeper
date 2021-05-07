package com.deviget.minesweeperapi.repository;

import com.deviget.minesweeperapi.repository.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    List<GameEntity> findByUserId(String userId);

}
