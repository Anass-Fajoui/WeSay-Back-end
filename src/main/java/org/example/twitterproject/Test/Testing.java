package org.example.twitterproject.Test;

import org.example.twitterproject.Auth.AuthenticationResponse;

import java.lang.reflect.Method;

public class Testing {
    public static void main(String[] args){
        Method[] methods = AuthenticationResponse.class.getDeclaredMethods();
        for (Method m : methods){
            System.out.println(m);
        }
    }
}
