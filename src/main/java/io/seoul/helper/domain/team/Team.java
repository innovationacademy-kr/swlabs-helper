package io.seoul.helper.domain.team;

import io.seoul.helper.domain.member.Member;
import io.seoul.helper.domain.project.Project;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "team")
    private List<Member> members;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Long maxMemberCount;

    @Column(name = "LOCATION")
    @Enumerated(value = EnumType.STRING)
    private TeamLocation location;

    @Column //TODO: change enum
    @Enumerated(value = EnumType.STRING)
    private TeamStatus status;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    public Long getCurrentMemberCount() {
        return new Long(members.size());
    }

}
