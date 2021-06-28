package io.seoul.helper.domain.team;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeamLocation {
    GAEPO("LOCATION_GAEPO", "개포동"),
    SEOCHO("LOCATION_SEOCHO", "서초동"),
    ONLINE("LOCATION_ONLINE", "온라인");

    private final String key;
    private final String name;
}
