package io.github.luidmidev.jakarta.validations.constraints.dimensions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageValidatorForFile extends ImageValidator<File> {

    @Override
    public InputStream getInputStream(File value) throws IOException {
        return new FileInputStream(value);
    }
}