package com.example.an.myapplication.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.an.myapplication.R;
import com.example.an.myapplication.models.Contact;

public final class ContactViewHolder extends RecyclerView.ViewHolder {
    private final TextView mNameTextView;
    private final TextView mNumberTextView;
    private final ImageView mProfileImageView;
    private final ImageView mCallImageView;
    private final View mItemView;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        mNameTextView = itemView.findViewById(R.id.contact_list_item_name_txt);
        mNumberTextView = itemView.findViewById(R.id.contact_list_item_number_txt);
        mProfileImageView = itemView.findViewById(R.id.contact_list_item_profile_image_view);
        mCallImageView = itemView.findViewById(R.id.contact_list_item_call_image_view);
        mItemView = itemView;
    }

    public void bindTo(Contact contact, View.OnClickListener onCallButtonClickListener, View.OnClickListener onItemClickListener) {
        mNameTextView.setText(contact.getName());
        mNumberTextView.setText(contact.getNumbers().isEmpty() ? null : contact.getNumbers().get(0));
        mProfileImageView.setImageDrawable(contact.getPhoto());
        mCallImageView.setOnClickListener(onCallButtonClickListener);
        mItemView.setOnClickListener(onItemClickListener);
    }
}
