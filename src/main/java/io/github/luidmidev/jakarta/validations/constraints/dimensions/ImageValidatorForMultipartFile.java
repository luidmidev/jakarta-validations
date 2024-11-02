package io.github.luidmidev.jakarta.validations.constraints.dimensions;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class ImageValidatorForMultipartFile extends ImageValidator<MultipartFile> {

    @Override
    public InputStream getInputStream(MultipartFile value) throws IOException {
        return value.getInputStream();
    }
}
