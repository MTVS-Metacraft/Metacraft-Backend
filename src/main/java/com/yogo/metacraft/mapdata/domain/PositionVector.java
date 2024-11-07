package com.yogo.metacraft.mapdata.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionVector extends Vector {

    @Column(name = "position_x")
    private Float x;

    @Column(name = "position_y")
    private Float y;

    @Column(name = "position_z")
    private Float z;

}
