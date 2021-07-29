package io.seoul.helper.domain.team;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Embedded
    private Period period;

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

    @Column
    private String subject;

    @Column
    private String description;

    public Long getCurrentMemberCount() {
        return new Long(members.size());
    }


    public void updateTeam(Period period, Long maxMemberCount, TeamLocation location, Project project) {
        this.period = period;
        this.maxMemberCount = maxMemberCount;
        this.location = location;
        this.project = project;
    }

    public void outTeam() {
        if (this.getCurrentMemberCount().equals(this.maxMemberCount)) {
            this.updateTeamReady();
        }
    }

    public void joinTeam() {
        if (this.getCurrentMemberCount().equals(this.maxMemberCount - 1)) {
            this.updateTeamFull();
        } else {
            this.updateTeamReady();
        }
    }

    public void endTeam() {
        if (this.getCurrentMemberCount().equals(this.maxMemberCount - 1)) {
            this.updateTeamFull();
        } else {
            this.updateTeamReady();
        }
    }

    private void updateTeamFull() {
        this.status = TeamStatus.FULL;
    }

    private void updateTeamReady() {
        this.status = TeamStatus.READY;
    }

    public void updateTeamEnd() {
        this.status = TeamStatus.END;
    }

    public void updateTeamRevoke() {
        this.status = TeamStatus.REVOKE;
    }

    public void updateTeamTimeout() {
        this.status = TeamStatus.TIMEOUT;
    }

    @Builder
    public Team(Long id, Period period, Long maxMemberCount,
                TeamLocation location, TeamStatus status, Project project, String description, String subject) {
        this.period = period;
        this.maxMemberCount = maxMemberCount;
        this.location = location;
        this.status = status;
        this.project = project;
        this.subject = subject;
        this.description = description;
    }


}
