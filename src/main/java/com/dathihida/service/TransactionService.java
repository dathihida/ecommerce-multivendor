package com.dathihida.service;

import com.dathihida.model.Order;
import com.dathihida.model.Transaction;

public interface TransactionService {
    Transaction createTransaction(Order order);
}
