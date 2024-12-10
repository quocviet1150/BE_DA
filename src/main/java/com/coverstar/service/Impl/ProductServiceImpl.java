package com.coverstar.service.Impl;

import com.coverstar.constant.Constants;
import com.coverstar.entity.Comment;
import com.coverstar.entity.Discount;
import com.coverstar.entity.Image;
import com.coverstar.entity.Product;
import com.coverstar.repository.CommentRepository;
import com.coverstar.repository.DiscountRepository;
import com.coverstar.repository.ImageRepository;
import com.coverstar.repository.ProductRepository;
import com.coverstar.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${image.directory}")
    private String imageDirectory;

    @Override
    public Product saveOrUpdateProduct(Long id,
                                       String productName,
                                       Long brandId,
                                       Long quantity,
                                       BigDecimal price,
                                       BigDecimal priceBeforeDiscount,
                                       String color,
                                       String size,
                                       String description,
                                       List<MultipartFile> imageFiles,
                                       List<Long> imageIdsToRemove) throws Exception {
        try {
            Product product;
            if (id != null) {
                product = productRepository.getProductById(id);
                product.setUpdatedDate(new Date());
            } else {
                product = new Product();
                product.setCreatedDate(new Date());
                product.setStatus(true);
            }
            product.setProductName(productName);
            product.setBrandId(brandId);
            product.setQuantity(quantity);
            product.setPrice(price);
            product.setPriceBeforeDiscount(priceBeforeDiscount);
            product.setColor(color);
            product.setSize(size);
            product.setDescription(description);

            if (imageIdsToRemove != null && !imageIdsToRemove.isEmpty()) {
                for (Long imageId : imageIdsToRemove) {
                    Image image = imageRepository.findImageById(imageId);
                    File file = new File(image.getDirectoryPath());
                    if (file.exists()) {
                        file.delete();
                    }
                    imageRepository.deleteById(imageId);
                }
            }
            product = productRepository.save(product);
            return saveImageProduct(imageFiles, product);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    private Product saveImageProduct(List<MultipartFile> imageFiles, Product product) throws Exception {
        try {
            if (imageFiles != null && !imageFiles.isEmpty()) {
                Set<Image> images = new HashSet<>();
                for (MultipartFile file : imageFiles) {
                    String filePath = imageDirectory + "products" + File.separator + product.getId();
                    File directory = new File(filePath);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    String fullPath = filePath + File.separator + file.getOriginalFilename();
                    file.transferTo(new File(fullPath));

                    Image image = new Image();
                    image.setProductId(product.getId());
                    image.setDirectoryPath(fullPath);
                    image.setType(Integer.valueOf(Constants.Number.ONE));
                    images.add(image);
                }
                product.setImages(images);
            }
            return productRepository.save(product);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public List<Product> findByNameAndPriceRange(Long brandId, String name, BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            String nameValue = name != null ? name : StringUtils.EMPTY;
            BigDecimal minPriceValue = minPrice != null ? minPrice : BigDecimal.ZERO;
            BigDecimal maxPriceValue = maxPrice != null ? maxPrice : BigDecimal.valueOf(Double.MAX_VALUE);
            return productRepository.findByNameContainingAndPriceBetweenWithDetails(brandId, nameValue, minPriceValue, maxPriceValue);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Product getProductById(Long id) {
        try {
            Product product = productRepository.getProductById(id);
            Set<Comment> comments = commentRepository.findCommentById(id);
            if (CollectionUtils.isEmpty(comments)) {
                comments = new HashSet<>();
            }
            product.setComments(comments);

            long numberOfVisits;
            if (product.getNumberOfVisits() == null) {
                numberOfVisits = 1L;
            } else {
                numberOfVisits = product.getNumberOfVisits() + 1;
            }
            product.setNumberOfVisits(numberOfVisits);
            productRepository.save(product);
            return product;
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteProductById(Long id) {
        try {
            Product product = productRepository.getProductById(id);
            Set<Comment> commentLasts = commentRepository.findCommentById(id);
            if (CollectionUtils.isEmpty(commentLasts)) {
                commentLasts = new HashSet<>();
            }
            product.setComments(commentLasts);

            Set<Comment> comments = product.getComments();
            if (comments != null && !comments.isEmpty()) {
                for (Comment comment : comments) {
                    Set<Image> images = comment.getImages();
                    for (Image image : images) {
                        File file = new File(image.getDirectoryPath());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }

            Set<Image> images = product.getImages();
            if (images != null && !images.isEmpty()) {
                for (Image image : images) {
                    File file = new File(image.getDirectoryPath());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            productRepository.deleteById(id);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Product updateStatus(Long id, Boolean type) {
        try {
            Product product = productRepository.getProductById(id);
            product.setStatus(type);
            return productRepository.save(product);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }
}