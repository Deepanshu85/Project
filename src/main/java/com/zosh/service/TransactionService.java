package com.zosh.service;

import com.zosh.modal.Order;
import com.zosh.modal.Seller;
import com.zosh.modal.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
