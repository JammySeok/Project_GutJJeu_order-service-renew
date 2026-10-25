package jammy.orderrenew.service;

import jammy.orderrenew.model.dto.ProductInfo;
import jammy.orderrenew.model.dto.RemainStockInfo;

import java.util.List;

public interface DeductStockService {

    Long decreaseStock(String optionCode, int quantity);
    List<RemainStockInfo> decreaseStocks (List<ProductInfo> request);

    void rollBackStock(String optionCode, int quantity);
    void rollBackStocks(List<ProductInfo> request);
}
