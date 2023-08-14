package mm.com.mytelpay.adapter.common.api.business;

import mm.com.mytelpay.adapter.common.api.database.entity.ProductInquiryLogEntity;

import java.util.Optional;

public interface ProductInquiryLogService {
    ProductInquiryLogEntity saveProductInquiryLog(ProductInquiryLogEntity requestLog);

    Optional<ProductInquiryLogEntity> findById(String id);
}
