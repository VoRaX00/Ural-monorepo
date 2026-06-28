package ru.ural.contracts.services;

import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ural.exceptions.InternalServerException;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final JsonMapper jsonMapper;

    public String objectToString(Object object) {
        try {
            return jsonMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new InternalServerException("Invalid mapping object to string", e);
        }
    }

}
