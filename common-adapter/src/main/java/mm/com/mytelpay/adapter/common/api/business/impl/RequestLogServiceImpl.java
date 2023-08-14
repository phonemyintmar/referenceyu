package mm.com.mytelpay.adapter.common.api.business.impl;

import lombok.extern.slf4j.Slf4j;

import mm.com.mytelpay.adapter.common.api.business.RequestLogService;
import mm.com.mytelpay.adapter.common.api.database.entity.RequestLogEntity;
import mm.com.mytelpay.adapter.common.api.database.repository.RequestLogRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@Primary
public class RequestLogServiceImpl implements RequestLogService {
    private final RequestLogRepository requestLogRepository;

    public RequestLogServiceImpl(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    public RequestLogEntity saveLog(RequestLogEntity requestLog) {
        return this.requestLogRepository.save(requestLog);
    }

    @Override
    public Optional<RequestLogEntity> findByRefTransId(String refTransId) {
        return this.requestLogRepository.findByRefTransId(refTransId);
    }

    @Override
    public Optional<RequestLogEntity> findByRefProductInquiryId(String refProductInquiryId) {
        return this.requestLogRepository.findByRefProductInquiryId(refProductInquiryId);
    }

    /**
     * Catch exception in here, If we have problem with update database, we still return true error
     * code
     */
    public void updateLog(Long id, RequestLogEntity requestLogEntity) {
        try {
            requestLogRepository.save(requestLogEntity);
        } catch (Exception exception) {
            log.error("Can not update log on transGw: {}", exception.getMessage());
        }
    }
}
