package com.edubank.model;


import lombok.Data;
import javax.validation.Valid;

@Data
public class RequestWrapper<T> {

    @Valid
    private T request;
}