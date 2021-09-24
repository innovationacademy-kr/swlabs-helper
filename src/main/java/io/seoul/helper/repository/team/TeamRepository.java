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
import java.util.List;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t " +
            "WHERE (t.status IN (:statusList)) and " +
            "(:location is null or t.location = :location) and " +
            "(:startTimePrevious is null or t.period.startTime > :startTimePrevious) and " +
            "(:endTimePrevious is null or t.period.endTime > :endTimePrevious)")
    Page<Team> findTeamsByQueryParameters(LocalDateTime startTimePrevious, LocalDateTime endTimePrevious,
                                          List<TeamStatus> statusList, TeamLocation location, Pageable pageable);

    @Query("SELECT DISTINCT t FROM Team t " +
            "WHERE (t.status in (:statusList)) and " +
            "(:location is null or t.location = :location) and " +
            "(:startTimePrevious is null or t.period.startTime > :startTimePrevious) and " +
            "(:endTimePrevious is null or t.period.endTime > :endTimePrevious) and " +
            "t.id IN :teamId")
    Page<Team> findTeamsByTeamIdIn(LocalDateTime startTimePrevious, LocalDateTime endTimePrevious,
                                   List<TeamStatus> statusList, TeamLocation location, List<Long> teamId,
                                   Pageable pageable);

    @Query("SELECT DISTINCT t FROM Team t " +
            "WHERE (t.status in (:statusList)) and " +
            "(:location is null or t.location = :location) and " +
            "(:startTimePrevious is null or t.period.startTime > :startTimePrevious) and " +
            "(:endTimePrevious is null or t.period.endTime > :endTimePrevious) and " +
            "t.id NOT IN :teamId")
    Page<Team> findTeamsByTeamIdNotIn(LocalDateTime startTimePrevious, LocalDateTime endTimePrevious,
                                      List<TeamStatus> statusList, TeamLocation location, List<Long> teamId, Pageable pageable);

    @Query("SELECT DISTINCT t FROM Team t " +
            "WHERE (t.status = :status) and " +
            "(:currentTime is null or t.period.endTime <= :currentTime)")
    List<Team> findTeamsByStatusNotAndEndTimeLessThan(TeamStatus status, LocalDateTime currentTime);


    @Query("SELECT count(t) from Team t " +
            "WHERE (t.created between :start and :end) and " +
            "(t.status IN :statusList)")
    Integer findTeamCountByCreateTimeRangeAndStatus(LocalDateTime start, LocalDateTime end, Set<TeamStatus> statusList);

    @Query("SELECT count(t) from Team t " +
            "WHERE (t.updated between :start and :end) and " +
            "(t.status IN :statusList)")
    Integer findTeamCountByUpdatedTimeRangeAndStatus(LocalDateTime start, LocalDateTime end, Set<TeamStatus> statusList);

    @Query("SELECT count(t) from Team t " +
            "WHERE (t.period.startTime between :start and :end) and " +
            "(t.status IN :statusList)")
    Integer findTeamCountByStartTimeRangeAndStatus(LocalDateTime start, LocalDateTime end, Set<TeamStatus> statusList);

    @Query("SELECT count(t) from Team t " +
            "WHERE (t.period.endTime between :start and :end) and " +
            "(t.status IN :statusList)")
    Integer findTeamCountByEndTimeRangeAndStatus(LocalDateTime start, LocalDateTime end, Set<TeamStatus> statusList);
}
