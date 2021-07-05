package io.seoul.helper.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultResponseDto<T> {
    private Integer statusCode;
    private String message;
    private T data;

    @Builder
    public ResultResponseDto(Integer statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }
}
