package com.br.fundamentosandroid.exceptions;

public class UserAlreadyRegistered extends Exception {

    public UserAlreadyRegistered(String detailMessage) {
        super(detailMessage);
    }
}
