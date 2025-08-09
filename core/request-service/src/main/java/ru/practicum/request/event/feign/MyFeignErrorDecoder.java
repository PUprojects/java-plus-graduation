package ru.practicum.request.event.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exception_handler.NotFoundException;

@Slf4j
public class MyFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if(response.status() == 404) {
            //if(methodKey == "")
            log.error("Метод {}, событие не найдено", methodKey);
            return new NotFoundException("Событие не найдено.");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
