package mm.com.mytelpay.adapter.common.api.database.repository;

import mm.com.mytelpay.adapter.common.api.database.entity.ClientEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {}
