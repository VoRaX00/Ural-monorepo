package ural.ru.services.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ural.ru.properties.proxy.NotificationProperty;

import java.net.URI;

@Service("notificationProxyService")
public class NotificationProxyService extends ProxyService {

    protected NotificationProxyService(RestTemplate restTemplate, NotificationProperty notificationProperty) {
        super(restTemplate, notificationProperty);
    }

    @Override
    public ResponseEntity<?> processProxyRequest(byte[] body, HttpMethod method, HttpServletRequest request) {
        URI uri = buildURI(request);
        return sendRequest(uri, body, method, request);
    }

}
