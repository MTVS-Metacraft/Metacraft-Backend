package com.yogo.metacraft.mapdata.repository;

import com.yogo.metacraft.mapdata.domain.MapData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapDataRepository extends JpaRepository<MapData, Long> {
}
