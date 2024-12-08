package com.coverstar.service.Impl;

import com.coverstar.constant.DateUtill;
import com.coverstar.entity.Account;
import com.coverstar.entity.Brand;
import com.coverstar.entity.Discount;
import com.coverstar.repository.AccountRepository;
import com.coverstar.repository.DiscountRepository;
import com.coverstar.service.DiscountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${image.directory}")
    private String imageDirectory;

    @Override
    public Discount createOrUpdateDiscount(Long id, String name, String code,
                                           String description, BigDecimal percent, MultipartFile imageFile,
                                           String expiredDate, List<Long> userIds, Integer discountType,
                                           BigDecimal levelApplied) throws Exception {
        Discount discount = new Discount();
        try {

            boolean isCodeExist = id == null
                    ? discountRepository.existsByCode(code)
                    : discountRepository.existsByCodeAndIdNot(code, id);

            if (isCodeExist) {
                throw new Exception("Discount code already exists");
            }

            if (id != null) {
                discount = discountRepository.findById(id).orElse(null);
                discount.setUpdatedDate(new Date());
                discount.setStatus(true);
            } else {
                discount.setCreatedDate(new Date());
                discount.setStatus(true);
            }
            discount.setName(name);
            discount.setCode(code);
            discount.setPercent(percent);
            discount.setDescription(description);
            discount.setExpiredDate(DateUtill.parseDate(expiredDate));
            discount.setDiscountType(discountType);
            discount.setLevelApplied(levelApplied);
            if (userIds != null && !userIds.isEmpty()) {
                Set<Account> accounts = new HashSet<>();
                for (Long usedId : userIds) {
                    Account account = accountRepository.findById(usedId).orElse(null);
                    if (discount != null) {
                        accounts.add(account);
                    }
                }
                discount.setAccounts(accounts);
            }
            discount = discountRepository.save(discount);

            if (imageFile != null && !imageFile.isEmpty()) {
                if (discount.getDirectoryPath() != null) {
                    File oldFile = new File(discount.getDirectoryPath());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                String fullPath = handleFileUpload(imageFile, "discount", discount.getId());
                discount.setDirectoryPath(fullPath);
            }
            discountRepository.save(discount);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return discount;
    }

    @Override
    public List<Discount> searchDiscount(String name, Boolean status, String code, Long accountId, Integer discountType) {
        List<Discount> discounts;
        try {
            String nameValue = name != null ? name : StringUtils.EMPTY;
            Boolean statusValue = status != null ? status : null;
            String codeValue = code != null ? code : StringUtils.EMPTY;
            discounts = discountRepository.findAllByStatus(nameValue, statusValue, codeValue, accountId, discountType);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return discounts;
    }

    @Override
    public Discount getDiscount(Long id) {
        try {
            return discountRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Long getByCode(String code) {
        try {
            return discountRepository.findByCode(code);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteDiscount(Long id) {
        try {
            Discount discount = discountRepository.findById(id).orElse(null);
            if (discount != null && discount.getDirectoryPath() != null) {
                File oldFile = new File(discount.getDirectoryPath());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            discountRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Discount updateStatus(Long id, boolean status) {
        try {
            Discount discount = discountRepository.findById(id).orElse(null);
            if (discount != null) {
                discount.setStatus(status);
                discount.setUpdatedDate(new Date());
                discountRepository.save(discount);
            }
            return discount;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String handleFileUpload(MultipartFile file, String type, Long id) throws Exception {
        String filePath = imageDirectory + type + File.separator + id;
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fullPath = filePath + File.separator + file.getOriginalFilename();
        file.transferTo(new File(fullPath));
        return fullPath;
    }
}