package com.zosh.service;

import com.zosh.modal.Seller;
import com.zosh.modal.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller sellerId);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
