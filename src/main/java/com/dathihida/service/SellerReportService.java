package com.dathihida.service;

import com.dathihida.model.Seller;
import com.dathihida.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
