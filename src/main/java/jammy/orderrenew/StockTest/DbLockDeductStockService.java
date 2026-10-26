package jammy.orderrenew.StockTest;

import jammy.orderrenew.exception.ErrorCode;
import jammy.orderrenew.exception.OutOfStockException;
import jammy.orderrenew.model.dto.ProductInfo;
import jammy.orderrenew.model.dto.RemainStockInfo;
import jammy.orderrenew.service.DeductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "custom.stock.strategy", havingValue = "db-lock")
public class DbLockDeductStockService implements DeductStockService {

    private final StockJpaRepository stockRepository;

    /*
     * 단건: 행 잠금 → 재고 검증 → 차감
     * createOrder의 @Transactional에 참여하므로, 주문 저장이 끝나 커밋될 때까지 락 유지
     */
    @Transactional
    public Long decreaseStock(String optionCode, int quantity) {
        StockEntity stock = stockRepository.findByOptionCodeForUpdate(optionCode)
                .orElseThrow(() -> new OutOfStockException(ErrorCode.OUT_OF_STOCK));

        if (stock.getStock() < quantity) {
            throw new OutOfStockException(ErrorCode.OUT_OF_STOCK);
        }
        stock.decrease(quantity);   // 변경 감지로 커밋 시 UPDATE
        return stock.getStock();
    }

    /*
     * 다건: 데드락 방지를 위해 optionCode 순으로 정렬한 뒤 잠금
     */
    @Transactional
    public List<RemainStockInfo> decreaseStocks(List<ProductInfo> request) {
        List<RemainStockInfo> result = new ArrayList<>();
        request.stream()
                .sorted(Comparator.comparing(ProductInfo::optionCode))
                .forEach(item -> result.add(new RemainStockInfo(
                        item.optionCode(),
                        decreaseStock(item.optionCode(), item.quantity())
                )));
        return result;
    }

    /*
     * DB 방식은 예외가 나면 트랜잭션 롤백으로 재고가 자동 복구되므로 별도 보상 처리 불필요
     */
    public void rollBackStock(String optionCode, int quantity) { }
    public void rollBackStocks(List<ProductInfo> request) { }
}