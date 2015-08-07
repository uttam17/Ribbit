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
public class LoginExceptionAlertFragment extends DialogFragment {
    String exceptionMessage;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        exceptionMessage = getArguments().getString("exception");
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.login_error_title)
                .setMessage(exceptionMessage)
                .setPositiveButton(context.getString(R.string.ok), null);

        return builder.create();
    }
}
