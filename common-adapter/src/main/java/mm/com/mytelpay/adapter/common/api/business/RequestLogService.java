package mm.com.mytelpay.adapter.common.api.business;

import mm.com.mytelpay.adapter.common.api.database.entity.RequestLogEntity;

import java.util.Optional;

public interface RequestLogService {

    RequestLogEntity saveLog(RequestLogEntity requestLog);

    Optional<RequestLogEntity> findByRefTransId(String refTransId);

    Optional<RequestLogEntity> findByRefProductInquiryId(String refTransId);

    void updateLog(Long id, RequestLogEntity requestLogEntity);
}
