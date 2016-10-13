package io.github.plastix.prolificlibrary.ui.add;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import io.github.plastix.prolificlibrary.R;

public class ExitDialog extends DialogFragment {

    private static final String ID = "ExitDialog";

    public static void show(AppCompatActivity activity) {
        ExitDialog dialog = new ExitDialog();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        // Only show a new Dialog if we don't already have one in the manager
        if (fragmentManager.findFragmentByTag(ID) == null) {
            dialog.show(activity.getFragmentManager(), ID);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.add_exit_dialog_title)
                .setMessage(R.string.add_exit_dialog_content)
                .setPositiveButton(R.string.add_exit_dialog_back, (dialog, which) -> {
                    dismiss();
                    getActivity().finish();
                })
                .setNegativeButton(R.string.add_exit_dialog_stay,
                        (dialog, which) -> dismiss())
                .setCancelable(false)
                .show();
    }
}
