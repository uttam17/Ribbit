package com.uttamapps.ribbit.alert_Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.uttamapps.ribbit.R;

/**
 * Created by Uttam Kumaran on 8/7/2015.
 */
public class LoginAlertFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.login_error_title))
                .setMessage(context.getString(R.string.login_error_message))
                .setPositiveButton(context.getString(R.string.ok), null);
        return builder.create();
    }
}
