package org.acme.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
        log.info("Incoming JSON parsing error: ", this);
    }

    public ServiceException(String format, Object... objects) {

        super(String.format(format, objects));
        log.info("Incoming JSON parsing error: ", this);
    }

}