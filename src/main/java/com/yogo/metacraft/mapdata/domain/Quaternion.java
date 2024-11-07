package com.yogo.metacraft.mapdata.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quaternion {

    @Column(name = "rotation_x")
    private Float x;

    @Column(name = "rotation_y")
    private Float y;

    @Column(name = "rotation_z")
    private Float z;

    @Column(name = "rotation_w")
    private Float w;
}