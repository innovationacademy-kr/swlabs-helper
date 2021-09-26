package io.seoul.helper.repository.settle;

import io.seoul.helper.domain.settle.Settle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettleRepository extends JpaRepository<Settle, Long> {
}
