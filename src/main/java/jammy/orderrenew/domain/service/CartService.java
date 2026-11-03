package jammy.orderrenew.domain.service;

import jammy.orderrenew.domain.model.dto.ProductInfo;
import jammy.orderrenew.domain.model.dto.request.ClientProductReq;
import jammy.orderrenew.domain.model.dto.response.CartItemRes;

import java.util.List;
import java.util.Map;

public interface CartService {

    CartItemRes addCartItem(Long userIdx, ProductInfo cartReq);
    CartItemRes plusCartItem(Long userIdx, String optionCode);
    CartItemRes minusCartItem(Long userIdx, String optionCode);
    void deleteCartItem(Long userIdx, String optionCode);
    void deleteCartAll(Long userIdx);
    List<CartItemRes> getCartList(Long userIdx);

    List<ClientProductReq> getProductByCart(Long userIdx);
    Map<Long, Integer> getCartItemQuantities(Long userIdx);
}
