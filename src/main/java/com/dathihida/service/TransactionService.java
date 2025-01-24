package com.dathihida.service;

import com.dathihida.model.Order;
import com.dathihida.model.Seller;
import com.dathihida.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);
    List<Transaction> getTransactionsBySellerId(Seller seller);
    List<Transaction> getAllTransactions();
}
