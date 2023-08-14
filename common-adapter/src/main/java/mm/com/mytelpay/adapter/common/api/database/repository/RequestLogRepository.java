package mm.com.mytelpay.adapter.common.api.database.repository;

import mm.com.mytelpay.adapter.common.api.database.entity.RequestLogEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLogEntity, Long> {

    @Query("from RequestLogEntity e where e.refTransId = :id")
    Optional<RequestLogEntity> findByRefTransId(String id);

    @Query("from RequestLogEntity e where e.refProductInquiryId = :id")
    Optional<RequestLogEntity> findByRefProductInquiryId(String id);
}
