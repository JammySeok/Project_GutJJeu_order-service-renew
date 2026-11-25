package jammy.orderrenew.global.dummy;

import jammy.orderrenew.StockTest.product.StockEntity;
import jammy.orderrenew.StockTest.product.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 서버 기동 시 테스트용 상품 옵션(재고 행)만 DB에 생성
 * - ddl-auto=create로 재기동하면 테이블이 비워지므로, 옵션 행이 항상 존재하도록 INSERT
 * - 재고 수량은 테스트마다 직접 세팅
 *     DB    : UPDATE stock SET stock = 100;
 *     Redis : MSET product:stock:OPT001 100 product:stock:OPT002 100 product:stock:OPT003 100
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DummyStockRunner implements ApplicationRunner {

    // 초기 재고 (행 생성용 기본값, 실제 테스트 수량은 직접 UPDATE)
    private static final long INITIAL_STOCK = 100;

    // 생성할 옵션 코드
    private static final List<String> OPTION_CODES = List.of(
            "OPT001",
            "OPT002",
            "OPT003"
    );

    private final StockJpaRepository stockRepository;

    @Override
    public void run(ApplicationArguments args) {

        List<StockEntity> stocks = OPTION_CODES.stream()
                .map(code -> new StockEntity(code, INITIAL_STOCK))
                .toList();
        stockRepository.saveAll(stocks);

        log.info("==== 더미 옵션 생성 완료: {} (재고 {}개) ====", OPTION_CODES, INITIAL_STOCK);
    }
}