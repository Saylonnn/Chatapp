package com.saylonn.chatapp;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;

import com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents.Contact;
import com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents.CustomSearchAdapter;

import java.util.ArrayList;


public class UnitTest {
    @Test
    public void ContactToGetUsername(){
        String name = "test";
        String mail = "test@gmail.com";
        String expected = "test";
        Contact test = new Contact(name, mail);
        String actual = test.getUsername();
        assertEquals(expected, actual);
    }
    @Test
    public void ContactToGetEmail(){
        String name = "test";
        String mail = "test@gmail.com";
        String expected ="test@gmail.com";
        Contact test = new Contact(name, mail);
        String actual = test.getEmail();
        assertEquals(expected, actual);
    }
    @Test
    public void CustomSearchAdapterToGetItemCount0Items(){
        ArrayList<Contact> list = new ArrayList<>();
        Context context = null;
        CustomSearchAdapter test = new CustomSearchAdapter(context , list);
        String expected = "0";
        String actual = String.valueOf(test.getItemCount());
        assertEquals(expected, actual);
    }
    @Test
    public void CustomSearchAdapterToGetItemCount2Items(){
        ArrayList<Contact> list = new ArrayList<>();
        String name = "test";
        String mail = "test@gmail.com";
        Contact contact = new Contact(name, mail);
        name = "tester";
        mail = "tester@gmail.com";
        Contact contact2 = new Contact(name, mail);
        list.add(contact);
        list.add(contact2);
        Context context = null;
        CustomSearchAdapter test = new CustomSearchAdapter(context, list);
        String expected = "2";
        String actual = String.valueOf(test.getItemCount());
        assertEquals(expected, actual);
    }
}