package com.firrael.rx.model;

/**
 * Created by Railag on 07.06.2016.
 */
public class Result {
    public String error;

    public boolean invalid() {
        return error != null;
    }
}
