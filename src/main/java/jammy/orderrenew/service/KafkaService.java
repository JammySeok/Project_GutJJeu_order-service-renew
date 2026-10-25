package jammy.orderrenew.service;

import jammy.orderrenew.model.dto.event.StockUpdateMsg;

public interface KafkaService {
    void sendStockUpdate(StockUpdateMsg stockUpdateMsg);

}
