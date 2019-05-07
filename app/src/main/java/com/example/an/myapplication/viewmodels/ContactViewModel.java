package com.example.an.myapplication.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.example.an.myapplication.models.Contact;
import com.example.an.myapplication.utilities.PhoneUtils;

import java.lang.ref.WeakReference;

public final class ContactViewModel extends AndroidViewModel {
    private MutableLiveData<Contact> mContact = new MutableLiveData<>();

    public ContactViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contact> getContact(String contactId) {
        new FetchContactAsyncTask(this, contactId).execute();
        return mContact;
    }

    private static class FetchContactAsyncTask extends AsyncTask<Void, Void, Contact> {
        final WeakReference<ContactViewModel> mContactViewModel;
        final String mContactId;

        FetchContactAsyncTask(ContactViewModel viewModel, String contactId) {
            mContactViewModel = new WeakReference<>(viewModel);
            mContactId = contactId;
        }

        @Override
        protected Contact doInBackground(Void... voids) {
            final ContactViewModel cvm = mContactViewModel.get();
            Contact contact = null;

            if (cvm == null) {
                return null;
            }

            final String[] projection = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.LOOKUP_KEY,
            };

            final String selection = ContactsContract.Contacts._ID + "=?";
            final String[] selectionArgs = { mContactId };

            final Cursor cursor = cvm.getApplication().getContentResolver()
                    .query(ContactsContract.Contacts.CONTENT_URI, projection,
                            selection,
                            selectionArgs,
                            null);

            if (cursor == null) {
                return null;
            }

            if (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                contact = new Contact(mContactId,
                        name,
                        PhoneUtils.fetchPhoneNumbers(cvm.getApplication(), mContactId),
                        PhoneUtils.fetchEmails(cvm.getApplication(), mContactId),
                        null);
            }

            cursor.close();

            return contact;
        }

        @Override
        protected void onPostExecute(Contact contact) {
            final ContactViewModel cvm = mContactViewModel.get();
            if (cvm != null) {
                cvm.mContact.postValue(contact);
            }
        }
    }
}
