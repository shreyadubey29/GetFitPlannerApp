package com.dubey.get_fit_planner_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShowDateChange extends DialogFragment {

    private Button buttonEnter;
    private Button buttonClear;
    private EditText newDateView;
    private String dateS;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_date_changer,null);

        buttonEnter = dialogView.findViewById(R.id.enter_button);
        buttonClear = dialogView.findViewById(R.id.clear_button);
        newDateView = dialogView.findViewById(R.id.changed_date);

        builder.setView(dialogView).setTitle("Edit Date");

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateS = newDateView.getText().toString();
                LogDetailsActivity callingActivity =(LogDetailsActivity) getActivity();
                callingActivity.changeDate(dateS);
                dismiss();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newDateView.setText("");
            }
        });

        return builder.create();
    }
}
