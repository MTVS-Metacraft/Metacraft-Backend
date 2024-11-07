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
public class ScaleVector extends Vector {

    @Column(name = "scale_x")
    private Float x;

    @Column(name = "scale_y")
    private Float y;

    @Column(name = "scale_z")
    private Float z;
}