package io.seoul.helper.domain.team;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Team {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Long maxMemberCount;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TeamLocation location;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TeamStatus status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public Long getCurrentMemberCount() {
        return new Long(members.size());
    }

    
    public void updateTeam(LocalDateTime startTime, LocalDateTime endTime, Long maxMemberCount,
                           TeamLocation location, Project project) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxMemberCount = maxMemberCount;
        this.location = location;
        this.project = project;
    }

    public void updateTeamReady(LocalDateTime startTime, LocalDateTime endTime, Long maxMemberCount) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxMemberCount = maxMemberCount;
        this.status = TeamStatus.READY;
    }

    public void updateTeamEnd() {
        this.status = TeamStatus.END;
    }

    @Builder
    public Team(Long id, LocalDateTime startTime, LocalDateTime endTime, Long maxMemberCount,
                TeamLocation location, TeamStatus status, Project project) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxMemberCount = maxMemberCount;
        this.location = location;
        this.status = status;
        this.project = project;
    }
}
