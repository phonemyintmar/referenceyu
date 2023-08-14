package mm.com.mytelpay.adapter.common.api.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import mm.com.mytelpay.adapter.common.entity.AuditableEntity;
import mm.com.mytelpay.adapter.common.validator.ValidateConstraint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "CLIENT")
@NoArgsConstructor
public class ClientEntity extends AuditableEntity {

    private static final long serialVersionUID = 776150111636364267L;

    @Id
    @Column(name = "CLIENT_ID", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    private String clientId;

    @Column(name = "NAME", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH)
    private String name;

    @Column(name = "CLIENT_SECRET")
    private String clientSecret;

    @Column(name = "SCOPE", length = ValidateConstraint.LENGTH.SCOPE_MAX_LENGTH)
    private String scope;
}
