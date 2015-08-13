package com.uttamapps.ribbit.alert_Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.uttamapps.ribbit.R;

/**
 * Created by Uttam Kumaran on 8/8/2015.
 */
public class DefaultAlertFragment extends DialogFragment{

    String message;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        message = getArguments().getString("exception");
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok), null);
        return builder.create();
    }
}
