package io.seoul.helper;

import io.seoul.helper.domain.team.Period;
import io.seoul.helper.domain.team.Team;
import io.seoul.helper.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class HelperApplicationTests {

	@Test
	void contextLoads() {
		LocalDateTime test1 = LocalDateTime.now();
		LocalDateTime test2 = LocalDateTime.parse("2021-09-30T15:00:00");
		if (test1.isBefore(test2))
			System.out.println("HelperApplicationTests.contextLoads");
	}

}
