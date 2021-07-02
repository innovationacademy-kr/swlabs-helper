package io.seoul.helper.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultResponseDto<T> {
    private Integer statusCode;
    private String message;
    private Integer count;
    private T data;

    @Builder
    public ResultResponseDto(Integer statusCode, String message, Integer count, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.count = count;
        this.data = data;
    }
}
