package com.example.an.myapplication.activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.an.myapplication.R;
import com.example.an.myapplication.adapters.ContactsRecyclerAdapter;
import com.example.an.myapplication.viewmodels.ContactsViewModel;

import static com.example.an.myapplication.Constants.PERMISSIONS_REQUEST_READ_CONTACTS;

public final class ContactsActivity extends AppCompatActivity {
    private ContactsRecyclerAdapter mContactsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        setupRecyclerViewWithAdapter();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            fetchContactsAndUpdateUI();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                showInformation(getString(R.string.enable_contacts_permissions));
            } else {
                showInformation(getString(R.string.allow_contact_permissions));
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    private void setupRecyclerViewWithAdapter() {
        final RecyclerView recyclerView = findViewById(R.id.contacts_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mContactsRecyclerAdapter = new ContactsRecyclerAdapter();
        recyclerView.setAdapter(mContactsRecyclerAdapter);
    }

    private void fetchContactsAndUpdateUI() {
        final ContactsViewModel viewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
        final View progressBar = findViewById(R.id.progress_bar_container);
        viewModel.getLoadingStatus().observe(this, status -> {
            switch (status) {
                case LOADING_STARTED: {
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                }
                case LOADING_COMPLETED: {
                    progressBar.setVisibility(View.GONE);
                    break;
                }
            }
        });

        viewModel.getContacts().observe(this, contacts -> {
            if (contacts.isEmpty()) {
                showInformation(getString(R.string.you_have_no_contacts));
                return;
            }
            mContactsRecyclerAdapter.refill(contacts);
        });
    }

    private void showInformation(String info) {
        final TextView textView = findViewById(R.id.information_placeholder_txt);
        textView.setText(info);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchContactsAndUpdateUI();
                } else {
                    showInformation(getString(R.string.allow_contact_permissions));
                }
            }
        }
    }

    // Todo: update the screen ones user manually enables the permission from the settings and comes back.
    // Todo: optimize the contacts loading.
    // Todo: rename package :)
    // Todo: add appropriate comments.
}
