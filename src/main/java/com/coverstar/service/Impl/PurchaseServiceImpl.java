package com.coverstar.service.Impl;

import com.coverstar.constant.Constants;
import com.coverstar.dto.PurchaseDto;
import com.coverstar.entity.Purchase;
import com.coverstar.repository.PurchaseRepository;
import com.coverstar.service.AddressService;
import com.coverstar.service.ProductService;
import com.coverstar.service.PurchaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpNE;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;

    @Override
    public List<Purchase> createPurchase(List<PurchaseDto> purchaseDtos) {
        List<Purchase> purchases = new ArrayList<>();
        try {
            for (PurchaseDto purchaseDto : purchaseDtos) {
                Purchase purchase = new Purchase();
                purchase.setUserId(purchaseDto.getUserId());
                purchase.setProduct(productService.getProductById(purchaseDto.getProductId()));
                purchase.setQuantity(purchaseDto.getQuantity());
                purchase.setPaymentMethod(purchaseDto.getPaymentMethod());
                purchase.setAddress(addressService.getAddressById(purchaseDto.getAddressId()));
                purchase.setStatus(Integer.valueOf(Constants.Number.ONE));
                purchase.setFirstWave(Integer.valueOf(Constants.Number.ONE));
                purchase.setCreatedDate(new Date());
                purchase.setUpdatedDate(new Date());
                purchase.setColor(purchaseDto.getColor());
                purchase.setSize(purchaseDto.getSize());
                purchase.setTotal(purchaseDto.getTotal());
                purchase.setTotalAfterDiscount(purchaseDto.getTotalAfterDiscount());
                purchase.setQuantity(purchaseDto.getQuantity());
                purchase.setDescription(purchaseDto.getDescription());
                purchases.add(purchase);
            }
            return purchaseRepository.saveAll(purchases);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Purchase updateFirstWave(Long id, Long addressId) throws Exception {
        try {
            Purchase purchase = purchaseRepository.findById(id).orElse(null);
            if (purchase == null) {
                throw new Exception(Constants.PURCHASE_NOT_FOUND);
            }
            purchase.setAddress(addressService.getAddressById(addressId));
            purchase.setFirstWave(Integer.valueOf(Constants.Number.TWO));
            purchase.setUpdatedDate(new Date());
            return purchaseRepository.save(purchase);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Purchase updateStatus(Long id, Integer status) throws Exception {
        try {
            Purchase purchase = purchaseRepository.findById(id).orElse(null);
            if (purchase == null) {
                throw new Exception(Constants.PURCHASE_NOT_FOUND);
            }
            purchase.setStatus(status);
            purchase.setUpdatedDate(new Date());
            return purchaseRepository.save(purchase);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public List<Purchase> getPurchaseByUserId(Long userId, String productName) {
        try {
            String productNameValue = productName != null ? productName : StringUtils.EMPTY;
            return purchaseRepository.findAllByUserId(userId, productNameValue);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public List<Purchase> getAllPurchase(Long userId, String paymentMethod, Integer status) {
        try {
            Long userIdValue = userId != null ? userId : 0L;
            Integer statusValue = status != null ? status : 0;
            String paymentMethodValue = paymentMethod != null ? paymentMethod : StringUtils.EMPTY;
            return purchaseRepository.findAllByUserIdAndPaymentMethodContainingAndStatus(userIdValue, paymentMethodValue, statusValue);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }
}
