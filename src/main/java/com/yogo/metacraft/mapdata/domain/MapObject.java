package com.yogo.metacraft.mapdata.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 접근 제한
@AllArgsConstructor
@Builder
@ToString
@Table(name = "map_object")
public class MapObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objectName; // post로 mapName으로 요청하면 -> 검색기능, 검색 조회용으로 필요, return List<MapObject>
    private String prefabName; // modelNum

    @Embedded
    private PositionVector position;

    @Embedded
    private Quaternion rotation;

    @Embedded
    private ScaleVector scale;

    //json
}