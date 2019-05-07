package com.example.an.myapplication.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.an.myapplication.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.an.myapplication.Constants.PERMISSIONS_REQUEST_CALL_PHONE;

public final class PhoneUtils {
    private PhoneUtils() {
    }

    public static void startCall(Activity activity, String phoneNumber) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            final Intent intent = new Intent(Intent.ACTION_CALL)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CALL_PHONE)) {
                final String text = activity.getResources().getString(R.string.allow_call_phone_permissions);
                Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CALL_PHONE},
                        PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
    }

    public static void startMessage(Context context, String phoneNumber) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void startEmail(Context context, String email) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void renameContact(Context context, String contactId, String newName) {
        // Todo: rename contact.
    }

    public static void addPhoneNumber(Context context, String contactId, String number) {
        // Todo: add phone number.
    }

    public static void addEmail() {
        // Todo: add email.
    }

    public static ArrayList<String> fetchPhoneNumbers(Context context, String contactId) {
        final ArrayList<String> phones = new ArrayList<>();
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        final Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[]{String.valueOf(contactId)},
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                final int columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
                final String phoneNumber = cursor.getString(columnIndex);
                phones.add(phoneNumber);
            }
            cursor.close();
        }
        return phones;
    }

    public static ArrayList<String> fetchEmails(Context context, String contactId) {
        final ArrayList<String> emails = new ArrayList<>();
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.DATA};
        final Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection,
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[]{String.valueOf(contactId)},
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                final int emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                final String email = cursor.getString(emailColumnIndex);
                emails.add(email);
            }

            cursor.close();
        }
        return emails;
    }

    public static InputStream fetchPhoto(Context context, long contactId) {
        final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        final Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        final Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                final byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }
}
