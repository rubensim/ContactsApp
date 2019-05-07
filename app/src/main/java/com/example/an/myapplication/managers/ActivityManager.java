package com.example.an.myapplication.managers;

import android.content.Context;
import android.content.Intent;

import com.example.an.myapplication.activities.DetailsActivity;
import com.example.an.myapplication.models.Contact;

import static com.example.an.myapplication.Constants.KEY_CONTACT_ID;

public final class ActivityManager {
    public static void startDetailsActivity(Context context, Contact contact) {
        final Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY_CONTACT_ID, contact.getContactId());
        context.startActivity(intent);
    }
}
