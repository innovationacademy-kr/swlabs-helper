package io.seoul.helper.domain.settle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettleStatus {
    PASS("STATUS_PASS", "통과"),
    SHORT("STATUS_SHORT", "미달");

    private final String key;
    private final String name;
}
