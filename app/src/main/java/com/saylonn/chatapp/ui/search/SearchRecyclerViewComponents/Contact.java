package com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents;

public class Contact {
    private String username;
    private String email;

    /**
     * Konstruktor der Name und Mail dem Contact Objekt zuordnet
     * @param name - name des Nutzers
     * @param mail - Mail des Nutzers
     */
    public Contact(String name, String mail){
        username = name;
        email = mail;
    }

    /**
     * @return gibt den Username zurück
     */
    public String getUsername(){
        return username;
    }

    /**
     * @return gibt die Email zurück
     */
    public String getEmail(){
        return email;
    }
}
