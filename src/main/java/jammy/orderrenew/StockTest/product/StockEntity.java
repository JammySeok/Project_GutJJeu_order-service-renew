package jammy.orderrenew.StockTest.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비관적 락 테스트용
 * : 비교용 재고 테이블
 */

@Getter
@Entity
@Table(name = "stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockEntity {

    @Id
    @Column(name = "option_code")
    private String optionCode;

    @Column(name = "stock", nullable = false)
    private Long stock;

    public StockEntity(String optionCode, Long stock) {
        this.optionCode = optionCode;
        this.stock = stock;
    }

    public void decrease(int quantity) {
        this.stock -= quantity;
    }
}