package group.bison.netty.modules.oauth2.security.client;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

public class CustomJdbcClientDetailsService extends JdbcClientDetailsService {

    public CustomJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Cacheable(value = "local-cache", key = "#clientId", unless = "#result==null")
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return super.loadClientByClientId(clientId);
    }

    @Override
    @CacheEvict(value = "local-cache", key = "#clientId")
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        super.updateClientSecret(clientId, secret);
    }

    @Override
    @CacheEvict(value = "local-cache", key = "#clientDetails.getClientId()")
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        super.updateClientDetails(clientDetails);
    }
}
