package ural.ru.services.proxy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ural.ru.properties.proxy.CargoProperty;

import java.net.URI;

@Service("cargoProxyService")
public class CargoProxyService extends ProxyService {

    protected CargoProxyService(RestTemplate restTemplate, CargoProperty cargoProperty) {
        super(restTemplate, cargoProperty);
    }

    @Override
    public ResponseEntity<?> processProxyRequest(byte[] body, HttpMethod method, HttpServletRequest request) {
        URI uri = buildURI(request);
        return sendRequest(uri, body, method, request);
    }

}
