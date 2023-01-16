package com.saylonn.chatapp.ui.SearchRecyclerViewComponents;

public class Contact {
    private String username;
    private String email;

    public Contact(String name, String mail){
        username = name;
        email = mail;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public void setUsername(String name){
        username = name;
    }

    public void setEmail(String mail){
        email = mail;
    }
}
