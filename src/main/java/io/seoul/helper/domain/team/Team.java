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
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @Column(name = "team_start_time")
    private LocalDateTime startTime;

    @Column(name = "team_end_time")
    private LocalDateTime endTime;

    @Column(name = "team_max_member_count", nullable = false)
    private Long maxMemberCount;

    @Column(name = "team_location")
    @Enumerated(value = EnumType.STRING)
    private TeamLocation location;

    @Column(name = "team_status")
    @Enumerated(value = EnumType.STRING)
    private TeamStatus status;

    @ManyToOne
    @JoinColumn(name = "team_project_id")
    private Project project;

    public Long getCurrentMemberCount() {
        return new Long(members.size());
    }

    @Builder
    public Team(LocalDateTime startTime, LocalDateTime endTime, Long maxMemberCount, TeamLocation location, TeamStatus status, Project project) {
        this.members = members;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxMemberCount = maxMemberCount;
        this.location = location;
        this.status = status;
        this.project = project;
    }
}