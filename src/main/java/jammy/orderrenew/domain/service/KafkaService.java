package jammy.orderrenew.domain.service;

import jammy.orderrenew.domain.model.dto.event.StockUpdateMsg;

public interface KafkaService {
    void sendStockUpdate(StockUpdateMsg stockUpdateMsg);

}
