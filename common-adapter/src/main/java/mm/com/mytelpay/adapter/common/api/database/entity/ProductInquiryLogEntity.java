package mm.com.mytelpay.adapter.common.api.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_INQUIRY_LOG")
public class ProductInquiryLogEntity implements Serializable {

    private static final long serialVersionUID = -1198205119417232732L;

    @Id
    @Column(name = "ID")
    private String id;

    @Lob
    @Column(name = "PRODUCT_DATA")
    private String productData;

    @Column(name = "CREATED_TIME")
    private Instant createdTime;

    @Column(name = "SERVICE_ACCOUNT")
    private String serviceAccount;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "REQUEST_ID")
    private String requestId;
}
