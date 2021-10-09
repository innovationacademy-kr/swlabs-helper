package io.seoul.helper.domain.settle;

import io.seoul.helper.domain.common.BaseTime;
import io.seoul.helper.domain.review.Review;
import io.seoul.helper.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Settle extends BaseTime {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @OneToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SettleStatus status;

    @Column
    private Boolean walletPaid;

    @Builder
    public Settle(User admin, Review review, SettleStatus status, Boolean walletPaid) {
        this.admin = admin;
        this.review = review;
        this.status = status;
        this.walletPaid = walletPaid;
    }

    public boolean payWallet() {
        if (walletPaid)
            return false;
        walletPaid = true;
        return true;
    }
}
