package io.seoul.helper.controller.settle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettlePayRequestDto {
    private Long id;

    @Builder
    public SettlePayRequestDto(Long id) {
        this.id = id;
    }
}
