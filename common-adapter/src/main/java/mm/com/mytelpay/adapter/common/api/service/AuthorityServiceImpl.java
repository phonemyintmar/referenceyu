package mm.com.mytelpay.adapter.common.api.service;

import mm.com.mytelpay.adapter.common.ClientDTO;
import mm.com.mytelpay.adapter.common.api.database.entity.ClientEntity;
import mm.com.mytelpay.adapter.common.api.database.repository.ClientRepository;
import mm.com.mytelpay.adapter.common.webapp.security.AuthorityService;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class AuthorityServiceImpl implements AuthorityService {

    private final ClientRepository clientRepository;

    public AuthorityServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Cacheable(cacheNames = "client-authentication", key = "#clientId", condition = "#clientId != null", unless = "#result == null")
    public ClientDTO getClientById(String clientId) {
        Optional<ClientEntity> clientEntityOptional = clientRepository.findById(clientId);
        if (clientEntityOptional.isPresent()) {
            ClientEntity clientEntity = clientEntityOptional.get();
            return ClientDTO.builder()
                    .id(clientEntity.getClientId())
                    .secret(clientEntity.getClientSecret())
                    .scope(clientEntity.getScope())
                    .build();
        }

        return null;
    }
}
