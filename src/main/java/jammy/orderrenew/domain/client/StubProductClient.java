package jammy.orderrenew.domain.client;

import jammy.orderrenew.domain.model.dto.request.ClientProductReq;
import jammy.orderrenew.domain.model.dto.response.ClientProductRes;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Primary                 // Feign 빈보다 이 빈이 우선 주입됨
@Profile("loadtest")     // loadtest 프로필일 때만 활성화
@Component
public class StubProductClient implements ProductClient {

    @Override
    public ClientProductRes getProduct(String productCode, String optionCode) {
        return new ClientProductRes(
                1L, 1L, productCode, optionCode, 1L,
                "테스트상품", "기본옵션", BigDecimal.valueOf(10000), 0
        );
    }

    @Override
    public List<ClientProductRes> getProductList(List<ClientProductReq> req) {
        return req.stream().map(r -> getProduct(r.productCode(), r.optionCode())).toList();
    }

    @Override
    public Map<Long, Long> getSellersByProductIds(List<Long> productIds) {
        return Map.of();
    }
}