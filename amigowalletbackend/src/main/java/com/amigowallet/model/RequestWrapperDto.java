package com.amigowallet.model;

import javax.validation.Valid;

public class RequestWrapperDto<T> {

    @Valid
    private T request;

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }
}
