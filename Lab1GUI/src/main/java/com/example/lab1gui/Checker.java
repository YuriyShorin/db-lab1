package com.example.lab1gui;

public class Checker {
    public static boolean isNumeric(String something){
        try {
            Integer.parseInt(something);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
