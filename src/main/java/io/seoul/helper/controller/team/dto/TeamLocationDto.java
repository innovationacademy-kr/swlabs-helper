package io.seoul.helper.controller.team.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class TeamLocationDto implements Serializable {
    private Long id;
    private String name;

    @Builder
    public TeamLocationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
