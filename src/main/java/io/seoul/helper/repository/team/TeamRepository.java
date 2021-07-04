package io.seoul.helper.repository.team;

import io.seoul.helper.domain.team.Team;
import io.seoul.helper.domain.team.TeamLocation;
import io.seoul.helper.domain.team.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t " +
            "WHERE (:status is null or t.status = :status) and " +
            "(:location is null or t.location = :location) and " +
            "(:startTime is null or t.startTime > :startTime) and " +
            "(:endTime is null or t.endTime < :endTime)")
    Page<Team> findTeamsByQueryParameters(LocalDateTime startTime, LocalDateTime endTime,
                                          TeamStatus status, TeamLocation location, Pageable pageable);

    @Query("SELECT DISTINCT t FROM Team t " +
            "JOIN t.members m JOIN m.user u " +
            "WHERE (:status is null or t.status = :status) and " +
            "(:location is null or t.location = :location) and " +
            "(:startTime is null or t.startTime > :startTime) and " +
            "(:endTime is null or t.endTime < :endTime) and " +
            ":userNickname = u.nickname ")
    Page<Team> findTeamsByUserNickname(LocalDateTime startTime, LocalDateTime endTime, TeamStatus status,
                                       TeamLocation location, String userNickname, Pageable pageable);
}
