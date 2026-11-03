package jammy.orderrenew.global.dummy;

import jammy.orderrenew.StockTest.product.StockEntity;
import jammy.orderrenew.StockTest.product.StockJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 서버 기동 시 테스트용 재고를 DB와 Redis에 동시에 세팅
 * - DB: ddl-auto=create로 테이블이 비워지므로 새로 INSERT
 * - Redis: 서버를 재기동해도 값이 남으므로 매번 덮어쓰기 (SET)
 *
 * 실행 예시
 *   정합성: --custom.dummy.stock=100
 *   성능  : --custom.dummy.stock=10000000
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DummyStockRunner implements ApplicationRunner {

    private static final String STOCK_KEY = "product:stock:";

    private final StockJpaRepository stockRepository;
    private final StringRedisTemplate redisTemplate;

    // 초기 재고 (기본 100)
    @Value("${custom.dummy.stock:100}")
    private long initialStock;

    // 세팅할 옵션 코드 목록 (다건 주문 테스트용으로 여러 개)
    @Value("${custom.dummy.option-codes:OPT001,OPT002,OPT003}")
    private List<String> optionCodes;

    @Override
    public void run(ApplicationArguments args) {

        // 1. DB 재고 세팅 (이미 있으면 덮어쓰기)
        List<StockEntity> stocks = optionCodes.stream()
                .map(code -> new StockEntity(code, initialStock))
                .toList();
        stockRepository.saveAll(stocks);

        // 2. Redis 재고 세팅 (다른 키는 건드리지 않도록 재고 키만 SET)
        for (String code : optionCodes) {
            redisTemplate.opsForValue().set(STOCK_KEY + code, String.valueOf(initialStock));
        }

        log.info("==== 더미 재고 세팅 완료: {} → {}개 (DB + Redis) ====", optionCodes, initialStock);
    }
}