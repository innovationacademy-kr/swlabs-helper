package io.seoul.helper.controller.review;

import io.seoul.helper.config.aop.ApiControllerTryCatch;
import io.seoul.helper.config.auth.LoginUser;
import io.seoul.helper.config.auth.dto.SessionUser;
import io.seoul.helper.controller.dto.ResultResponseDto;
import io.seoul.helper.controller.review.dto.ReviewResponseDto;
import io.seoul.helper.controller.review.dto.ReviewUpdateRequestDto;
import io.seoul.helper.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @ApiControllerTryCatch
    @PostMapping(value = "/api/v1/review")
    public ResultResponseDto<?> updateReview(@LoginUser SessionUser user,
                                             @RequestBody ReviewUpdateRequestDto requestDto) throws Exception {
        reviewService.updateReview(user, requestDto);
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(null)
                .build();
    }

    @ApiControllerTryCatch
    @GetMapping(value = "/api/v1/review")
    public ResultResponseDto<?> findReviewByMember(@RequestParam Long memberId) throws Exception {
        ReviewResponseDto dto = reviewService.findReviewByMemberId(memberId);
        return ResultResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .message("OK")
                .data(dto)
                .build();
    }

    //TODO: 정산필요 목록 조회 필요 (남은 정산수, Review 업데이트가 가장 최신순으로 데이터 조회)

}
