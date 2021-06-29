package io.seoul.helper.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultResponseDto<T> {
    private Long statusCode;
    private Long count;
    private T data;

    @Builder
    public ResultResponseDto(Long statusCode, Long count, T data) {
        this.statusCode = statusCode;
        this.count = count;
        this.data = data;
    }
}
