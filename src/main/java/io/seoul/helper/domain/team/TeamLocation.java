package io.seoul.helper.domain.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamLocation {
    GAEPO(0L, "LOCATION_GAEPO", "개포동"),
    SEOCHO(1L, "LOCATION_SEOCHO", "서초동"),
    ONLINE(2L, "LOCATION_ONLINE", "온라인");

    private final Long id;
    private final String key;
    private final String name;
}
