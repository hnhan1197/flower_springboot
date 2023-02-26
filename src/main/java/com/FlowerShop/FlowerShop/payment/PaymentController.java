package com.FlowerShop.FlowerShop.payment;

import com.FlowerShop.FlowerShop.models.Receipt;
import com.FlowerShop.FlowerShop.payment.PaymentConfig.PaymentIntent;
import com.FlowerShop.FlowerShop.payment.PaymentConfig.PaymentMethod;
import com.FlowerShop.FlowerShop.repositories.ReceiptRepository;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PaymentController {
    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private PaymentService paymentService;
//    @GetMapping("/payment")
//    public String paypal(){
//        return "paypal";
//    }

//    @PostMapping("/pay")
//    public String pay(HttpServletRequest request, @RequestParam("price") double price ){
//        String cancelUrl = PaymentUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
//        String successUrl = PaymentUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
//        try {
//            Payment payment = paymentService.createPayment(
//                    price,
//                    "USD",
//                    PaymentMethod.paypal,
//                    PaymentIntent.sale,
//                    "payment description",
//                    cancelUrl,
//                    successUrl);
//            for(Links links : payment.getLinks()){
//                if(links.getRel().equals("approval_url")){
//                    return "redirect:" + links.getHref();
//                }
//            }
//        } catch (PayPalRESTException e) {
//            log.error(e.getMessage());
//        }
//        return "redirect:/";
//    }
    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(){
        return "cancel";
    }
    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                Receipt receipt=receiptRepository.getReceiptByPaypalId(paymentId);
                receipt.setPaid(true);
                receiptRepository.save(receipt);
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}
