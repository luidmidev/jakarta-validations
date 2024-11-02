package io.github.luidmidev.jakarta.validations.constraints.dimensions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class ImageValidatorForArraysOfByte extends ImageValidator<byte[]> {

    @Override
    public InputStream getInputStream(byte[] value) {
        return new ByteArrayInputStream(value);
    }
}