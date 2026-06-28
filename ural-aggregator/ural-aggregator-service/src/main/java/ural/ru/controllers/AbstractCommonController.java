package ural.ru.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ural.ru.services.proxy.ProxyService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public abstract class AbstractCommonController {

    @Getter
    private final ProxyService proxyService;

    protected AbstractCommonController(Map<String, ProxyService> proxyServiceMap) {
        String name = getProxyServiceName();
        proxyService = Objects.requireNonNull(proxyServiceMap.get(name), "Proxy service not found: " + name);
    }

    protected ResponseEntity<?> sendAndReceive(
            byte[] body,
            HttpMethod httpMethod,
            HttpServletRequest httpServletRequest
    ) {
        return proxyService.processProxyRequest(body, httpMethod, httpServletRequest);
    }

    protected ResponseEntity<?> upload(List<MultipartFile> files, List<String> types, HttpServletRequest request) {
        return proxyService.upload(files, types, request);
    }

    protected ResponseEntity<?> uploadAvatar(MultipartFile file, String metadata, HttpServletRequest request) {
        return proxyService.uploadAvatar(file, metadata, request);
    }

    protected abstract String getProxyServiceName();

}
