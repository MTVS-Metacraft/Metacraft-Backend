package com.yogo.metacraft.mapdata.document;

import lombok.Data;

import java.util.List;

@Data
public class JsonParent {
    private String name;
    private List<Object> json_CodeBlocks;
}
