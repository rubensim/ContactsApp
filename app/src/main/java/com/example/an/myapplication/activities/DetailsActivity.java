package com.example.an.myapplication.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.an.myapplication.Constants;
import com.example.an.myapplication.R;
import com.example.an.myapplication.customviews.ExpandableCardView;
import com.example.an.myapplication.managers.DialogManager;
import com.example.an.myapplication.utilities.PhoneUtils;
import com.example.an.myapplication.viewmodels.ContactViewModel;

import java.util.ArrayList;

public final class DetailsActivity extends AppCompatActivity {
    private String mContactId;
    private ContactViewModel mViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContactId = getIntent().getStringExtra(Constants.KEY_CONTACT_ID);
        final ExpandableCardView expandablePhoneView = findViewById(R.id.expandable_phone_card_view);
        final ExpandableCardView expandableEmailView = findViewById(R.id.expandable_email_card_view);

        mViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        mViewModel.getContact(mContactId).observe(this, contact -> {
            if (contact == null) return;
            setupOwnerNameVew(mContactId, contact.getName());
            setupPhoneView(expandablePhoneView, contact.getNumbers());
            setupEmailView(expandableEmailView, contact.getEmails());
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupOwnerNameVew(String contactId, String name) {
        final View container = findViewById(R.id.contact_details_owner_name_with_edit_container);
        final TextView nameTxt = container.findViewById(R.id.contact_details_owner_name_txt);

        nameTxt.setText(name);
        container.setOnClickListener(view -> DialogManager.showAlertDialog(view.getContext(),
                R.string.change_contact_name, name, newName -> {
            PhoneUtils.renameContact(view.getContext(), contactId, newName);
            mViewModel.getContact(mContactId);
        }));
    }

    private void setupPhoneView(ExpandableCardView container, ArrayList<String> numbers) {
        container.removeAddedChildViews();
        final Context context = container.getContext();
        final View headerView = createExpandableCardHeaderView(context, context.getString(R.string.phone));
        container.addHeaderView(headerView);
        headerView.setOnClickListener(view -> DialogManager.showAlertDialog(view.getContext(),
                R.string.add_phone_number, "", newNumber -> {
                    PhoneUtils.addPhoneNumber(context, mContactId, newNumber);
                    mViewModel.getContact(mContactId);
                }));

        for (String number : numbers) {
            final View bodyView = createExpandableCardBodyPhoneView(context, number);
            bodyView.setOnClickListener(view -> {
                PhoneUtils.startCall(this, number);
            });
            container.addBodyView(bodyView);
        }
    }

    private void setupEmailView(ExpandableCardView container, ArrayList<String> emails) {
        container.removeAddedChildViews();
        final Context context = container.getContext();
        final View headerView = createExpandableCardHeaderView(context, context.getString(R.string.email));

        container.addHeaderView(headerView);
        headerView.setOnClickListener(view -> DialogManager.showAlertDialog(view.getContext(),
                R.string.add_email, "", newEmail -> {
                    PhoneUtils.addEmail();
                    mViewModel.getContact(mContactId);
                }));

        for (String email : emails) {
            final View bodyView = createExpandableCardBodyEmailView(context, email);
            container.addBodyView(bodyView);
        }
    }

    private View createExpandableCardHeaderView(Context context, String title) {
        final View view = View.inflate(context,
                R.layout.expandable_card_view_child_header_view, null);

        final TextView textView = view.findViewById(R.id.text_view);
        textView.setText(title);
        return view;
    }

    private View createExpandableCardBodyPhoneView(Context context, String phoneNumber) {
        final View view = View.inflate(context,
                R.layout.expandable_card_view_child_body_phone_view, null);

        final TextView textView = view.findViewById(R.id.text_view);
        textView.setText(phoneNumber);

        final ImageView messageImageView = view.findViewById(R.id.expandable_card_view_child_body_message_image_view);
        messageImageView.setOnClickListener(view1 -> PhoneUtils.startMessage(context, phoneNumber));

        return view;
    }

    private View createExpandableCardBodyEmailView(Context context, String email) {
        final View view = View.inflate(context,
                R.layout.expandable_card_view_child_body_email_view, null);

        final TextView textView = view.findViewById(R.id.expandable_card_view_child_body_email_txt);
        textView.setText(email);

        view.setOnClickListener(v -> PhoneUtils.startEmail(context, email));

        return view;
    }
}

