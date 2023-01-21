package com.saylonn.chatapp;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

import com.saylonn.chatapp.ui.search.SearchRecyclerViewComponents.Contact;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SearchContactTest {
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
}