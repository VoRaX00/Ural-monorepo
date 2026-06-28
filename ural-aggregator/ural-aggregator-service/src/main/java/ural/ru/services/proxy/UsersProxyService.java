package ural.ru.services.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ural.ru.properties.proxy.UsersProperty;

import java.net.URI;

@Service("usersProxyService")
public class UsersProxyService extends ProxyService {

    protected UsersProxyService(RestTemplate restTemplate, UsersProperty usersProperty) {
        super(restTemplate, usersProperty);
    }

    @Override
    public ResponseEntity<?> processProxyRequest(byte[] body, HttpMethod method, HttpServletRequest request) {
        URI uri = buildURI(request);
        return sendRequest(uri, body, method, request);
    }

}
