package io.seoul.helper.service;

import io.seoul.helper.controller.project.dto.ProjectDto;
import io.seoul.helper.domain.project.Project;
import io.seoul.helper.repository.project.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepo;

    @Transactional(readOnly = true)
    public List<ProjectDto> findAllProjects() {
        List<Project> list = projectRepo.findAll();
        return list.stream().map((o) -> ProjectDto.builder()
                .id(o.getId())
                .name(o.getName())
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDto findProject(Long projectId) throws EntityNotFoundException {
        Project project = projectRepo.findById(projectId).
                orElseThrow(() -> new EntityNotFoundException("invalid project"));
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .build();
    }

}
