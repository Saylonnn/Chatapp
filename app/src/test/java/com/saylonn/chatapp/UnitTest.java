package com.saylonn.chatapp;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;

import com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents.Contact;
import com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents.CustomSearchAdapter;

import java.util.ArrayList;


public class UnitTest {
    @Test
    public void Contact_To_GetUsername(){
        String username = "test";
        String email = "test@gmail.com";
        String expected = "test";
        Contact test = new Contact(username, email);
        String actual = test.getUsername();
        assertEquals(expected, actual);
    }
    @Test
    public void Contact_To_GetEmail(){
        String username = "test";
        String email = "test@gmail.com";
        String expected ="test@gmail.com";
        Contact test = new Contact(username, email);
        String actual = test.getEmail();
        assertEquals(expected, actual);
    }
    @Test
    public void CustomSearchAdapter_To_GetItemCount_0Items(){
        ArrayList<Contact> list = new ArrayList<>();
        Context context = null;
        CustomSearchAdapter test = new CustomSearchAdapter(context , list);
        String expected = "0";
        String actual = String.valueOf(test.getItemCount());
        assertEquals(expected, actual);
    }
    @Test
    public void CustomSearchAdapter_To_GetItemCount_2Items(){
        ArrayList<Contact> list = new ArrayList<>();
        String username = "test";
        String email = "test@gmail.com";
        Contact contact = new Contact(username, email);
        username = "tester";
        email = "tester@gmail.com";
        Contact contact2 = new Contact(username, email);
        list.add(contact);
        list.add(contact2);
        Context context = null;
        CustomSearchAdapter test = new CustomSearchAdapter(context, list);
        String expected = "2";
        String actual = String.valueOf(test.getItemCount());
        assertEquals(expected, actual);
    }
}