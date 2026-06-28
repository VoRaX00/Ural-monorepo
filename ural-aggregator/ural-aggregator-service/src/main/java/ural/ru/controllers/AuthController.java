package ural.ru.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ural.ru.services.proxy.ProxyService;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthController extends AbstractCommonController {

    protected AuthController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PostMapping("/refresh-tokens")
    public ResponseEntity<?> refresh(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PreAuthorize("hasAnyRole('URAL_ANY')")
    @PostMapping("/logout/all")
    public ResponseEntity<?> logoutAll(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "authProxyService";
    }
}
