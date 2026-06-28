package ural.ru.services.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ural.ru.properties.proxy.ContractProperty;

import java.net.URI;

@Service("contractProxyService")
public class ContractProxyService extends ProxyService {

    protected ContractProxyService(RestTemplate restTemplate, ContractProperty contractProperty) {
        super(restTemplate, contractProperty);
    }

    @Override
    public ResponseEntity<?> processProxyRequest(byte[] body, HttpMethod method, HttpServletRequest request) {
        URI uri = buildURI(request);
        return sendRequest(uri, body, method, request);
    }

}
