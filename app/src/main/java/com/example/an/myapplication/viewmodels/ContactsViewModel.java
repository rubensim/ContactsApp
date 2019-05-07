package com.example.an.myapplication.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.example.an.myapplication.R;
import com.example.an.myapplication.enums.LoadingStatus;
import com.example.an.myapplication.models.Contact;
import com.example.an.myapplication.utilities.PhoneUtils;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.provider.ContactsContract.Contacts;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

public final class ContactsViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Contact>> mContacts = new MutableLiveData<>();
    private MutableLiveData<LoadingStatus> mLoadingProgress = new MutableLiveData<>();

    public ContactsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Contact>> getContacts() {
        new FetchContactsAsyncTask(this).execute();
        return mContacts;
    }

    public LiveData<LoadingStatus> getLoadingStatus() {
        return mLoadingProgress;
    }

    private static class FetchContactsAsyncTask extends AsyncTask<Void, Void, ArrayList<Contact>> {
        final WeakReference<ContactsViewModel> mContactsViewModel;

        FetchContactsAsyncTask(ContactsViewModel viewModel) {
            mContactsViewModel = new WeakReference<>(viewModel);
        }

        @Override
        protected void onPreExecute() {
            final ContactsViewModel cvm = mContactsViewModel.get();
            cvm.mLoadingProgress.postValue(LoadingStatus.LOADING_STARTED);
        }

        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            final ContactsViewModel cvm = mContactsViewModel.get();
            final ArrayList<Contact> contacts = new ArrayList<>();

            if (cvm == null) {
                return contacts;
            }

            final String[] projection = new String[]{
                    Contacts._ID,
                    Contacts.DISPLAY_NAME_PRIMARY,
                    Contacts.LOOKUP_KEY,
            };

            final String selection = null;
            final String[] selectionArgs = null;

            final Cursor cursor = cvm.getApplication().getContentResolver()
                    .query(Contacts.CONTENT_URI, projection,
                            selection,
                            selectionArgs,
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

            if (cursor == null) {
                return contacts;
            }

            while (cursor.moveToNext()) {
                String contactId = cursor.getString(cursor.getColumnIndex(Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));

                final InputStream photo = PhoneUtils.fetchPhoto(cvm.getApplication(), Long.parseLong(contactId));
                Drawable drawable;
                if (photo != null) {
                    final RoundedBitmapDrawable roundedBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(cvm.getApplication().getResources(), photo);
                    roundedBitmapDrawable.setCircular(true);
                    drawable = roundedBitmapDrawable;
                } else {
                    drawable = ContextCompat.getDrawable(cvm.getApplication().getApplicationContext(), R.drawable.person);
                    drawable.setAlpha(54);
                }

                contacts.add(new Contact(contactId, name,
                        PhoneUtils.fetchPhoneNumbers(cvm.getApplication(), contactId),
                        PhoneUtils.fetchEmails(cvm.getApplication(), contactId),
                        drawable));
            }

            cursor.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            final ContactsViewModel cvm = mContactsViewModel.get();
            if (cvm != null) {
                cvm.mLoadingProgress.postValue(LoadingStatus.LOADING_COMPLETED);
                cvm.mContacts.postValue(contacts);
            }
        }
    }
}
