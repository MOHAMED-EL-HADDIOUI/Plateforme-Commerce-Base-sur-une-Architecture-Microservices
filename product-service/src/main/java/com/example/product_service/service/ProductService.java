package com.example.product_service.service;

import com.example.product_service.dto.inventoryDto.InventoryRequestDto;
import com.example.product_service.dto.inventoryDto.InventoryResponseDto;
import com.example.product_service.dto.inventoryDto.InventoryUpdateRequestDto;
import com.example.product_service.dto.productDto.ProductRequestDto;
import com.example.product_service.dto.productDto.ProductResponseDto;
import com.example.product_service.dto.productDto.ProductUpdateRequestDto;
import com.example.product_service.enums.Category;
import com.example.product_service.exception.InventoryNotFoundException;
import com.example.product_service.exception.ProductCreationException;
import com.example.product_service.external.InventoryClientService;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.model.Inventory;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.util.ProductMessage;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final InventoryClientService inventoryClientService;

    private final ProductMapper productMapper;

    // create
    @Transactional
    @CircuitBreaker(name = "inventoryServiceBreaker", fallbackMethod = "inventoryServiceFallback")
    @Retry(name = "inventoryServiceBreaker", fallbackMethod = "inventoryServiceFallback")
    @RateLimiter(name = "createProductLimiter", fallbackMethod = "inventoryServiceFallback")
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        log.info("Début de la création du produit");

        try {
            // Création du produit

            Product product = new Product();
            product.setName(productRequestDto.getName());
            product.setDescription(productRequestDto.getDescription());
            product.setPrice(productRequestDto.getPrice());
            product.setCategory(productRequestDto.getCategory());

            Inventory inventory= new Inventory();
            inventory.setStockQuantity(productRequestDto.getInventoryRequestDto().getStockQuantity());
            product.setInventory(inventory);
            Product savedProduct = productRepository.save(product);
            validateSavedProduct(savedProduct);

            // Création de l'inventaire
            InventoryResponseDto inventoryResponse = createInventoryForProduct(savedProduct);

            // Mise à jour du produit avec les informations d'inventaire
            updateProductWithInventory(savedProduct, inventoryResponse);

            log.info("Création du produit terminée avec succès");
            return productMapper.mapToProductResponseDto(savedProduct);
        } catch (Exception e) {
            log.error("Erreur lors de la création du produit", e);
            throw new ProductCreationException("Erreur lors de la création du produit", e);
        }
    }

    private void validateSavedProduct(Product savedProduct) {
        if (savedProduct.getId() == null) {
            log.error("L'ID du produit est null après la sauvegarde");
            throw new IllegalStateException("L'ID du produit ne devrait pas être null après la sauvegarde");
        }
    }

    private InventoryResponseDto createInventoryForProduct(Product product) {
        InventoryRequestDto inventoryRequestDto = productMapper.mapToInventoryRequestDto(product.getInventory());
        inventoryRequestDto.setProductId(product.getId());
        log.info("Création de l'inventaire pour le produit: {}", product.getId());
        return inventoryClientService.addInventory(inventoryRequestDto);
    }

    private void updateProductWithInventory(Product product, InventoryResponseDto inventoryResponse) {
        Inventory inventory = productMapper.mapToInventory(inventoryResponse);
        product.setInventory(inventory);
        product.setInventoryId(inventoryResponse.getId());
        productRepository.save(product);
    }




    // read
    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponseDto> getProductsAll() throws ServiceUnavailableException {
        log.info("ProductService::getProductsAll started");

        if (LocalDateTime.now().getHour() == 17) {
            throw new ServiceUnavailableException(ProductMessage.SERVICE_UNAVAILABLE_EXCEPTION);
        }
        List<Product> productList = productRepository.findAll();
        log.info("ProductService::getProductsAll finished");
        return productMapper.mapToProductResponseDtoList(productList);
    }

    // getProductById
    @Cacheable(value = "products", key = "#productId")
    public ProductResponseDto getProductById(String productId) {
        log.info("ProductService::getProductById started");

        Product product = getProduct(productId);

        log.info("ProductService::getProductById finished");
        return productMapper.mapToProductResponseDto(product);
    }

    // getInventoryById
    @Cacheable(value = "products", key = "#inventoryId")
    public ProductResponseDto getInventoryById(String inventoryId) {
        log.info("ProductService::getInventoryById started");
        Product product = productRepository.findByInventoryId(inventoryId).orElseThrow(()
                -> new InventoryNotFoundException(ProductMessage.INVENTORY_NOT_FOUND + inventoryId));

        log.info("ProductService::getInventoryById finished");
        return productMapper.mapToProductResponseDto(product);
    }

    // update
    @CircuitBreaker(name = "inventoryServiceBreaker", fallbackMethod = "inventoryServiceFallback")
    @Retry(name = "inventoryServiceBreaker", fallbackMethod = "inventoryServiceFallback")
    @RateLimiter(name = "createProductLimiter", fallbackMethod = "inventoryServiceFallback")
    @CacheEvict(value = "products", key = "#productUpdateRequestDto.id",allEntries = true)
    public ProductResponseDto updateProductById(ProductUpdateRequestDto productUpdateRequestDto) {
        log.info("ProductService::updateProductById started");

        Product product = getProduct(productUpdateRequestDto.getId());
        log.info("ProductService::updateProductById - product {}", product);


        Product updatedProduct = getUpdatedProduct(productUpdateRequestDto, product);
        log.info("ProductService::updateProductById - updatedProduct {}", updatedProduct);

        inventoryClientService.getInventoryById(productUpdateRequestDto.getInventoryUpdateRequestDto().getInventoryId());

        Inventory inventory = updatedProduct.getInventory();
        inventory.setProductId(productUpdateRequestDto.getInventoryUpdateRequestDto().getProductId());
        inventory.setStockQuantity(productUpdateRequestDto.getInventoryUpdateRequestDto().getNewQuantity());
        InventoryUpdateRequestDto mapToInventoryUpdateRequestDto = productMapper.mapToInventoryUpdateRequestDto(inventory);

        log.info("ProductService::updateProductById - inventory {}", inventory);
        log.info("ProductService::updateProductById - mapToInventoryRequestDto {}", mapToInventoryUpdateRequestDto);


        inventoryClientService.updateInventory(updatedProduct.getInventoryId(),
                mapToInventoryUpdateRequestDto);

        log.info("ProductService::updateProductById finished");
        return productMapper.mapToProductResponseDto(updatedProduct);
    }


    // delete
    @CacheEvict(value = "products", key = "#productId",allEntries = true)
    public String deleteProductById(String productId) {
        log.info("ProductService::deleteProductById started");

        Product product = getProduct(productId);

        getInventoryById(product.getInventoryId());

        productRepository.deleteById(product.getId());

        // ürün silindiği zaman stok takibide silinmelidir.
        inventoryClientService.deleteInventory(product.getId());

        log.info("ProductService::deleteProductById finished");
        return "successful deleted ";
    }


    // Get product names by price range using stream api
    @Cacheable(value = "productsByPriceRange", key = "'range:' + #minPrice + '-' + #maxPrice")
    public List<ProductResponseDto> getProductByPriceRange(double minPrice, double maxPrice) {
        log.info("ProductService::getProductNamesByPriceRange started");
        Sort sort = Sort.by("price").ascending() ;
//        Sort.by("price").descending();
        List<Product> productList = productRepository.findByPriceBetween(minPrice,maxPrice,sort);


        log.info("ProductService::getProductNamesByPriceRange finished");
        return productMapper.mapToProductResponseDtoList(productList);
    }


    @Cacheable(value = "productsByPrice", key = "'greaterOrEqual:' + #price")
    public List<ProductResponseDto> getProductByPriceGreaterThanEqual(double price) {
        log.info("ProductService::getProductNamesByPriceGreaterThanEqual started");
        Sort sort = Sort.by("price").ascending() ;
//        Sort.by("price").descending();
        List<Product> productList = productRepository.findByPriceGreaterThanEqual(price,sort);


        log.info("ProductService::getProductNamesByPriceGreaterThanEqual finished");
        return productMapper.mapToProductResponseDtoList(productList);
    }


    @Cacheable(value = "productsByPrice", key = "'lessOrEqual:' + #price")
    public List<ProductResponseDto> getProductByPriceLessThanEqual(double price) {
        log.info("ProductService::getProductNamesByPriceLessThanEqual started");
        Sort sort = Sort.by("price").ascending() ;
//        Sort.by("price").descending();
        List<Product> productList = productRepository.findByPriceLessThanEqual(price,sort);
        log.info("ProductService::getProductNamesByPriceLessThanEqual finished");
        return productMapper.mapToProductResponseDtoList(productList);

    }

    @Cacheable(value = "productsByQuantity", key = "#quantity")
    public List<ProductResponseDto> getProductByQuantity(int quantity) {
        log.info("ProductService::getProductByQuantity started");
        List<Product> productList = productRepository.findAll();
        log.info("ProductService::getProductByQuantity finish");
        return productList.stream()
                .filter(product -> product.getInventory().getStockQuantity() == quantity)
                .map(productMapper::mapToProductResponseDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "productsByCategory", key = "#category")
    public List<ProductResponseDto> getProductByCategory(String category) {
        log.info("ProductService::getProductByCategory started");

        Category categoryTypeEnum = null;
        try{
            categoryTypeEnum = Category.valueOf(category.toUpperCase());
            log.info("ProductResponseDto::getProductByCategory - categoryType : {}", categoryTypeEnum);
        }catch (IllegalArgumentException exception)
        {
            log.error("Product category type  provided: {}",category);
            throw new IllegalArgumentException("Product category type: " + categoryTypeEnum);
        }
        List<Product> productList = productRepository.findByCategory(categoryTypeEnum);

        log.info("ProductService::getProductByCategory finish");
        return productList.stream()
                .map(productMapper::mapToProductResponseDto)
                .collect(Collectors.toList());
    }




    private Product getProduct(String productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException(ProductMessage.PRODUCT_NOT_FOUND + productId));
    }

    private Product getUpdatedProduct(ProductUpdateRequestDto productUpdateRequestDto, Product product) {
        product.setName(productUpdateRequestDto.getName());
        product.setDescription(productUpdateRequestDto.getDescription());
        product.setPrice(productUpdateRequestDto.getPrice());
        product.setCategory(productUpdateRequestDto.getCategory());
        return productRepository.save(product);
    }

    private ProductResponseDto inventoryServiceFallback(Exception exception) {
        log.info("fallback is executed because servise is down :{}", exception.getMessage());
        return ProductResponseDto.builder()
                .name("IPHONE 13")
                .description("This product is created as a fallback because some service is down")
                .build();
    }

}
