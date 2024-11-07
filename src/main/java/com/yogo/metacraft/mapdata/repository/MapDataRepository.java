package com.yogo.metacraft.mapData.repository;

import com.yogo.metacraft.mapData.domain.MapData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapDataRepository extends JpaRepository<MapData, Long> {
}
