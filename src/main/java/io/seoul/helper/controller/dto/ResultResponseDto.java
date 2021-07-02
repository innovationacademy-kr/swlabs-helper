package io.seoul.helper.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultResponseDto<T> {
    private Integer statusCode;
    private Integer count;
    private T data;

    @Builder
    public ResultResponseDto(Integer statusCode, Integer count, T data) {
        this.statusCode = statusCode;
        this.count = count;
        this.data = data;
    }
}
