package io.seoul.helper.repository.settle;

import io.seoul.helper.domain.settle.Settle;
import io.seoul.helper.domain.settle.SettleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SettleRepository extends JpaRepository<Settle, Long> {
    List<Settle> findSettlesByStatusInAndWalletPaid(Collection<SettleStatus> status, boolean walletPaid);
}
