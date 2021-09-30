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


    @Query("SELECT DISTINCT t FROM Team t " +
            "WHERE (t.status NOT IN (:revoke, :ended)) and " +
            "((t.period.startTime is null or (t.period.startTime <= :startTime and t.period.endTime <= :endTime and t.period.endTime > :startTime)) or" +
            "(t.period.endTime is null or (t.period.startTime >= :startTime and t.period.endTime >= :endTime and t.period.startTime < :endTime)))")
    List<Team> findTeamsByDuplicateDateTime(TeamStatus revoke, TeamStatus ended, LocalDateTime startTime, LocalDateTime endTime);
}
