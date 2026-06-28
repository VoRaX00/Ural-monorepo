package ural.ru.controllers.users;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ural.ru.controllers.AbstractCommonController;
import ural.ru.services.proxy.ProxyService;

import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "Контроллер для управления пользователями")
public class UserController extends AbstractCommonController {

    protected UserController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getByUuid(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> update(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "usersProxyService";
    }

}
