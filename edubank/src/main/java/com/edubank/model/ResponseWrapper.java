package com.edubank.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseWrapper<T> {

    private T response;
    private List<Error> errors = new ArrayList<>();
}
