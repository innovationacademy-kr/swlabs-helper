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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class SettleApiController {
    private SettleService settleService;

    @ApiControllerTryCatch
    @PostMapping("settle")
    public ResponseEntity<?> postSettle(@LoginUser SessionUser user, @RequestBody SettlePostRequestDto dto) throws Exception {
        SettleResponseDto responseDto = settleService.postSettle(user, dto);
        ResultResponseDto rst = ResultResponseDto.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("CREATED")
                .data(responseDto)
                .build();
        return ResponseEntity.created(URI.create("/api/v1/settle/" + responseDto.getId())).body(rst);
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
}
