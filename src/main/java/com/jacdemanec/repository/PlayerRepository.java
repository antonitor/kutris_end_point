package com.jacdemanec.repository;

import com.jacdemanec.model.AppPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<AppPlayer, Long> {

    public AppPlayer findByGpgsId(String gpgsId);
}
