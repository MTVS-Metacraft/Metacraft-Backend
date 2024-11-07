package com.yogo.metacraft.mapdata.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vector {

    private Float x;
    private Float y;
    private Float z;
}