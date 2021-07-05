package io.seoul.helper.domain.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamLocation {
    NONE(0L, "LOCATION_NONE", "상관없음"),
    GAEPO(1L, "LOCATION_GAEPO", "개포동"),
    SEOCHO(2L, "LOCATION_SEOCHO", "서초동"),
    ONLINE(3L, "LOCATION_ONLINE", "온라인");

    private final Long id;
    private final String key;
    private final String name;
}
