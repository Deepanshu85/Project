package com.zosh.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.zosh.modal.Order;
import com.zosh.modal.PaymentOrder;
import com.zosh.modal.User;

import java.util.Set;

public interface PaymentService {

    PaymentOrder createOrder(User user , Set<Order>orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,String paymentId,String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorpayPaymentLink(User user,Long amount,Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user, Long amount,Long orderId) throws StripeException;
}
