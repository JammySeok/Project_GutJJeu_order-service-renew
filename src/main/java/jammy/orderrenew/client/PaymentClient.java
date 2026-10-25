package jammy.orderrenew.client;

import jammy.orderrenew.model.dto.request.ClientPaymentReq;
import jammy.orderrenew.model.dto.request.ClientRefundReq;
import jammy.orderrenew.model.dto.response.ClientPaymentRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * FeignClient
 * Payment-Service에 API 요청
 */
@FeignClient(name = "payment-service", path = "/client/payments")
public interface PaymentClient {

    /*
     * 결제 요청 (POST)
     * @param paymentRequest: 결제 요청 정보
     */
    @PostMapping("/process")
    ClientPaymentRes processPayment(
            @RequestBody ClientPaymentReq paymentRequest
    );

    /*
     * 환불 요청 (POST)
     * @param refundRequest: 환불 요청 정보
     */
    @PostMapping("/refund")
    ClientPaymentRes refundPayment(
            @RequestBody ClientRefundReq refundRequest
    );

}