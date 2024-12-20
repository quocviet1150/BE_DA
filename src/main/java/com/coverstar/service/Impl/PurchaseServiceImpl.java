package com.coverstar.service.Impl;

import com.coverstar.constant.Constants;
import com.coverstar.dto.PurchaseDto;
import com.coverstar.entity.*;
import com.coverstar.repository.*;
import com.coverstar.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserVisitRepository userVisitRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private DiscountService discountService;

    @Override
    public List<Purchase> createPurchase(List<PurchaseDto> purchaseDtos) throws Exception {
        List<Purchase> purchases = new ArrayList<>();
        try {
            for (PurchaseDto purchaseDto : purchaseDtos) {
                Purchase purchase = new Purchase();

                // check if discount is valid
                discountService.getDiscount(purchaseDto.getDiscountId(), 1);

                Product product = productService.getProductById(purchaseDto.getProductId());
                if (product.getQuantitySold() == null) {
                    product.setQuantitySold(0L);
                }

                product.setQuantitySold(product.getQuantitySold() + purchaseDto.getQuantity());
                product = productRepository.save(product);

                ProductDetail productDetail = productDetailRepository.getById(purchaseDto.getProductDetailId());
                if (productDetail.getQuantity() <  purchaseDto.getQuantity()) {
                    throw new Exception(Constants.INSUFFICIENT_PRODUCT_QUANTITY);
                }
                productDetail.setQuantity(productDetail.getQuantity() - purchaseDto.getQuantity());
                productDetail = productDetailRepository.save(productDetail);

                Category category = categoryService.getCategoryById(product.getCategoryId());
                if (category.getQuantitySold() == null) {
                    category.setQuantitySold(0L);
                }
                category.setQuantitySold(category.getQuantitySold() + purchaseDto.getQuantity());
                categoryRepository.save(category);

                UserVisits userVisits = userVisitRepository.findByVisitDate(new Date(), 2);
                if (userVisits == null) {
                    userVisits = new UserVisits();
                    userVisits.setVisitDate(new Date());
                    userVisits.setVisitCount(1L);
                    userVisits.setType(2);
                    userVisitRepository.save(userVisits);
                } else {
                    userVisits.setVisitCount(userVisits.getVisitCount() + 1);
                    userVisitRepository.save(userVisits);
                }

                purchase.setUserId(purchaseDto.getUserId());
                purchase.setProduct(product);
                purchase.setProductDetail(productDetail);
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
            if (status == 5) {
                Product product = productService.getProductById(purchase.getProduct().getId());
                if (product.getQuantitySold() < purchase.getQuantity()) {
                    throw new Exception(Constants.ERROR);
                }
                product.setQuantitySold(product.getQuantitySold() - purchase.getQuantity());
                productRepository.save(product);

                ProductDetail productDetail = productDetailRepository.getById(purchase.getProductDetail().getId());
                if (productDetail.getQuantity() <  productDetail.getQuantity()) {
                    throw new Exception(Constants.INSUFFICIENT_PRODUCT_QUANTITY);
                }
                productDetail.setQuantity(productDetail.getQuantity() + purchase.getQuantity());
                productDetailRepository.save(productDetail);
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
