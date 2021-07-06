package io.seoul.helper.controller.project.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Data
@NoArgsConstructor
public class ProjectDto implements Serializable {
    private Long id;
    private String name;

    @Builder
    public ProjectDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
