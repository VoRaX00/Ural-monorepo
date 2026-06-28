package ural.ru.controllers.files;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ural.ru.controllers.AbstractCommonController;
import ural.ru.services.proxy.ProxyService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
@Tag(name = "Контроллер для управления файлами")
public class FileController extends AbstractCommonController {

    protected FileController(Map<String, ProxyService> proxyServiceMap) {
        super(proxyServiceMap);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("types") List<String> types,
            HttpServletRequest request
    ) {
        return upload(files, types, request);
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> avatar(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") String metadata,
            HttpServletRequest request
    ) {
        return uploadAvatar(file, metadata, request);
    }

    @GetMapping
    ResponseEntity<?> getFiles(
            @RequestBody(required = false) byte[] body,
            HttpMethod method,
            HttpServletRequest request
    ) {
        return sendAndReceive(body, method, request);
    }

    @Override
    protected String getProxyServiceName() {
        return "fileProxyService";
    }

}
