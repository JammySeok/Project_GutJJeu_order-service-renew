package jammy.orderrenew.StockTest;

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
@Table(name = "Test_Stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockEntity {

    @Id
    @Column(name = "Option_Code")
    private String optionCode;

    @Column(name = "Stock", nullable = false)
    private Long stock;

    public void decrease(int quantity) {
        this.stock -= quantity;
    }
}