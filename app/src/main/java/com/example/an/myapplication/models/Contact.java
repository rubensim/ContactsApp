package com.example.an.myapplication.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public final class Contact {
    private String mContactId;
    private String mName;
    private ArrayList<String> mNumbers;
    private ArrayList<String> mEmails;
    private Drawable mPhoto;

    public Contact(String contactId, String name, ArrayList<String> numbers,
                   ArrayList<String> emails, Drawable photo) {
        mContactId = contactId;
        mName = name;
        mNumbers = numbers;
        mEmails = emails;
        mPhoto = photo;
    }

    public String getContactId() {
        return mContactId;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<String> getNumbers() {
        return mNumbers;
    }

    public ArrayList<String> getEmails() {
        return mEmails;
    }

    public Drawable getPhoto() {
        return mPhoto;
    }
}
