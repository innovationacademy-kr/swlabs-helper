package io.seoul.helper.domain.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewStatus {
    WAIT("STATUS_WAIT", "작성 대기중"),
    UPDATED("STATUS_UPDATED", "작성됨"),
    TIMEOUT("STATUS_TIMEOVER", "만료됨"),
    INVALID("STATUS_INVALID", "미참여");

    private final String key;
    private final String name;
}
