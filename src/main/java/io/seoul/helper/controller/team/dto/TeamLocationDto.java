package io.seoul.helper.controller.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class TeamLocationDto implements Serializable {
    private Long id;
    private String code;
    private String name;

    @Builder
    public TeamLocationDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
