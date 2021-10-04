package io.seoul.helper.controller.settle;

import io.seoul.helper.config.aop.ApiControllerTryCatch;
import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.settle.dto.SettlePostRequestDto;
import io.seoul.helper.controller.settle.dto.SettleResponseDto;
import io.seoul.helper.service.SettleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class SettleApiController {
    private SettleService settleService;

    @ApiControllerTryCatch
    @PostMapping("settle")
    public ResultResponseDto<?> postSettle(@LoginUser SessionUser user, SettlePostRequestDto dto,
                                           ServerHttpResponse responseHttp) throws Exception {
        SettleResponseDto responseDto = settleService.postSettle(user, dto);
        responseHttp.setStatusCode(HttpStatus.CREATED);
        responseHttp.getHeaders().add("Location", "/api/v1/settle/" + responseDto.getId());
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("CREATED")
                .data(responseDto)
                .build();
    }

    @ApiControllerTryCatch
    @GetMapping("settle/{id}")
    public ResultResponseDto<?> getSettle(@PathVariable Long id) throws Exception {
        SettleResponseDto responseDto = settleService.getSettle(id);
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(responseDto)
                .build();
    }

    //TODO: PAGEBLE 조회 필요

}
