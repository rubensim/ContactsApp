package com.example.an.myapplication.managers;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.example.an.myapplication.R;
import com.example.an.myapplication.interfaces.OnAlertDialogPositiveButtonClickListener;

public final class DialogManager {
    private DialogManager() {}

    public static void showAlertDialog(Context context, int title, String text,
                                       OnAlertDialogPositiveButtonClickListener onPositiveButtonClickListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.contact_alert_diaog_content_view, null);
        final EditText editText = view.findViewById(R.id.edit_text);
        editText.setText(text);
        editText.setSelection(text.length());

        builder.setPositiveButton(R.string.ok, (dialog, id) -> {
            onPositiveButtonClickListener.onClick(editText.getText().toString());
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());

        final AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.setTitle(title);
        dialog.show();
    }
}
