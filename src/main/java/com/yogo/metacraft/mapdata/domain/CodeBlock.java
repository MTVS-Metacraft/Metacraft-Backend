package com.yogo.metacraft.mapdata.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CodeBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codeBlockType;

    private String codeBlockName;

    @ElementCollection
    @CollectionTable(
            name = "code_block_input_data",
            joinColumns = @JoinColumn(name = "code_block_id")
    )
    @Column(name = "input_data")
    private List<String> inputDatas = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private List<CodeBlock> children = new ArrayList<>();
}