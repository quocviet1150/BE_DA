package com.coverstar.service.Impl;

import com.coverstar.repository.PurchaseRepository;
import com.coverstar.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;
}
