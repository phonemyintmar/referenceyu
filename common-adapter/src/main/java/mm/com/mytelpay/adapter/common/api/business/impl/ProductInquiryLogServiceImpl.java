package mm.com.mytelpay.adapter.common.api.business.impl;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.api.business.ProductInquiryLogService;
import mm.com.mytelpay.adapter.common.api.database.entity.ProductInquiryLogEntity;
import mm.com.mytelpay.adapter.common.api.database.repository.ProductInquiryLogRepository;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@Primary
public class ProductInquiryLogServiceImpl implements ProductInquiryLogService {
    private final ProductInquiryLogRepository productInquiryLogRepository;

    public ProductInquiryLogServiceImpl(ProductInquiryLogRepository productInquiryLogRepository) {
        this.productInquiryLogRepository = productInquiryLogRepository;
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "product-inquiry-log-id", key = "#result.getId()")
    public ProductInquiryLogEntity saveProductInquiryLog(ProductInquiryLogEntity requestLog) {
        return this.productInquiryLogRepository.save(requestLog);
    }

    @Override
    @Cacheable(
            cacheNames = "product-inquiry-log-id",
            key = "#id",
            condition = "#id != null",
            unless = "#id == null || #result == null")
    public Optional<ProductInquiryLogEntity> findById(String id) {
        return productInquiryLogRepository.findById(id);
    }
}
