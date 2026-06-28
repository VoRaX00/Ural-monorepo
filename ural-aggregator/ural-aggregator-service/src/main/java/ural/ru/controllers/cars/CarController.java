package ural.ru.controllers.cars;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ural.ru.controllers.AbstractCommonController;
import ural.ru.services.proxy.ProxyService;

import java.util.Map;

@RestController
@RequestMapping("/cars")
@Tag(name = "Контроллер для управления машинами")
public class CarController extends AbstractCommonController {

    protected CarController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @GetMapping("/by-vin/{vin}")
    public ResponseEntity<?> getByVin(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @GetMapping
    public ResponseEntity<?> getByFilters(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @GetMapping("/dictionaries")
    public ResponseEntity<?> getDictionaries(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @PostMapping("/{id}/photo-analysis/retry")
    public ResponseEntity<?> retryPhotoAnalysis(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "carProxyService";
    }
}
