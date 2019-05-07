package com.example.an.myapplication.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.an.myapplication.R;
import com.example.an.myapplication.managers.ActivityManager;
import com.example.an.myapplication.models.Contact;
import com.example.an.myapplication.utilities.PhoneUtils;
import com.example.an.myapplication.viewholders.ContactViewHolder;

import java.util.ArrayList;

public final class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private ArrayList<Contact> mData = new ArrayList<>();

    public void refill(ArrayList<Contact> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_recycler_view_item, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int position) {
        final Contact contact = mData.get(position);
        contactViewHolder.bindTo(contact, new OnCallButtonClickListener(contact), new OnItemClickListener(contact));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private static final class OnCallButtonClickListener implements View.OnClickListener {
        private final Contact mContact;

        OnCallButtonClickListener(Contact contact) {
            mContact = contact;
        }

        @Override
        public void onClick(View view) {
            if (mContact.getNumbers().isEmpty()) return;

            if (view.getContext() instanceof Activity) {
                PhoneUtils.startCall((Activity) view.getContext(), mContact.getNumbers().get(0));
            }
        }
    }

    private static final class OnItemClickListener implements View.OnClickListener {
        private final Contact mContact;

        OnItemClickListener(Contact contact) {
            mContact = contact;
        }

        @Override
        public void onClick(View view) {
            ActivityManager.startDetailsActivity(view.getContext(), mContact);
        }
    }
}
