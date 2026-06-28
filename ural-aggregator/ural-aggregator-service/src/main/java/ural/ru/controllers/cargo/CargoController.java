package ural.ru.controllers.cargo;

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
@RequestMapping("/cargo")
@Tag(name = "Контроллер для управления грузами")
public class CargoController extends AbstractCommonController {

    protected CargoController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER,ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @GetMapping
    public ResponseEntity<?> getPaginatedList(
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER,ADMIN,UNDERWRITER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "cargoProxyService";
    }
}
