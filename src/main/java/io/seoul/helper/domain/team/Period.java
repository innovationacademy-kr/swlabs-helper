package io.seoul.helper.domain.team;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
public class Period {
    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Builder
    public Period(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(startTime) || now.isEqual(startTime))
            return false;
        if (now.isAfter(endTime) || now.isEqual(endTime))
            return false;
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime))
            return false;
        return true;
    }

    public boolean isInRanged(Period target) {
        if (startTime.isAfter(target.startTime) || endTime.isBefore(target.endTime))
            return false;
        return true;
    }
}
