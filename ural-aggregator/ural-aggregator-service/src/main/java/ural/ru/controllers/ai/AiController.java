package ural.ru.controllers.ai;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ural.ru.controllers.AbstractCommonController;
import ural.ru.services.proxy.ProxyService;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController extends AbstractCommonController {

    protected AiController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @GetMapping("/recommendations/cargo/{cargoId}/cars")
    public ResponseEntity<?> getRecommendedCars(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @GetMapping("/recommendations/cars/{carId}/cargo")
    public ResponseEntity<?> getRecommendedCargo(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "aiProxyService";
    }

}
