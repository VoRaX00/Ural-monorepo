package ru.ural.models;

import org.springframework.http.HttpStatusCode;

public record HttpResponse<T>(T body, HttpStatusCode status, Exception exception) {

}
