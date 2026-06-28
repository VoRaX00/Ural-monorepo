package ural.ru.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/contracts")
@Tag(name = "Контроллер для управления контрактами")
public class ContractController extends AbstractCommonController {

    protected ContractController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
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
    public ResponseEntity<?> getByPaginationList(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @Operation(summary = "Изменить контракт")
    @PutMapping("/{id}")
    ResponseEntity<?> edit(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @PostMapping("/{id}/price-offer")
    public ResponseEntity<?> offerPrice(
            @RequestBody byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('USER, ADMIN')")
    @PostMapping("/{id}/rating")
    public ResponseEntity<?> rateContract(
            @RequestBody byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @GetMapping("/users/{userUuid}/completed")
    public ResponseEntity<?> getUserCompletedContracts(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "contractProxyService";
    }
}
