package com.example.morselight;

public class Items {
    private char name;
    private String code;
    public Items(char name,String code){
        this.name=name;
        this.code=code;
    }
    public void setName(char name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public char getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
