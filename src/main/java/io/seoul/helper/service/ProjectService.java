package io.seoul.helper.service;

import io.seoul.helper.controller.project.dto.ProjectDto;
import io.seoul.helper.domain.project.Project;
import io.seoul.helper.repository.project.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;

    public List<ProjectDto> findAllProjects() {
        List<Project> list = projectRepo.findAll();
        return list.stream().map((o) -> {
            return ProjectDto.builder()
                    .id(o.getId())
                    .name(o.getName())
                    .build();
        }).collect(Collectors.toList());
    }
}
