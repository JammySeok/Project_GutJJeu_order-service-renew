package jammy.orderrenew.StockTest;

import jammy.orderrenew.domain.model.dto.ProductInfo;
import jammy.orderrenew.domain.model.dto.RemainStockInfo;
import jammy.orderrenew.domain.service.impl.DeductStockServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 부하 테스트용: 다건 재고 차감 비교
 * - POST /test/stocks/deduct (Lua Script 1회, all-or-nothing)
 * - POST /test/stocks/deduct-legacy (DECR 반복 + 보상 롤백)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test/stocks")
// Redis 구현체가 등록될 때만 생성 (db-lock 모드에서는 Redis 구현체가 없어서 기동 에러 방지)
@ConditionalOnProperty(name = "custom.stock.strategy", havingValue = "redis", matchIfMissing = true)
public class StockDeductController {

    private final DeductStockServiceImpl deductStockService;

    /**
     * 다건 재고 차감 - Lua Script
     */
    @PostMapping("/deduct")
    public ResponseEntity<List<RemainStockInfo>> deduct(
            @RequestBody List<ProductInfo> request
    ) {
        return ResponseEntity.ok(deductStockService.decreaseStocks(request));
    }

    /**
     * 다건 재고 차감 - DECR 반복 + 보상 롤백 (이전 버전, 비교용)
     */
    @PostMapping("/deduct-legacy")
    public ResponseEntity<List<RemainStockInfo>> deductLegacy(
            @RequestBody List<ProductInfo> request
    ) {
        return ResponseEntity.ok(deductStockService.decreaseStocksLegacy(request));
    }
}