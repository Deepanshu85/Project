package com.zosh.modal;

import com.zosh.domain.PaymentOrderStatus;
import lombok.Data;

@Data
public class PaymentDetails {
    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkReferenceId;
    private String getRazorpayPaymentLinkStatus;
    private String getRazorpayPaymentIdZWSP;
    private PaymentOrderStatus status;
}
