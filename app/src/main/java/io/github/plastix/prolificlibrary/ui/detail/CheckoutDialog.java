package io.github.plastix.prolificlibrary.ui.detail;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import io.github.plastix.prolificlibrary.R;
import io.github.plastix.prolificlibrary.databinding.DialogCheckoutBinding;

public class CheckoutDialog extends DialogFragment {

    private static final String ID = "CheckoutDialog";
    private DialogCheckoutBinding binding;

    public static void show(AppCompatActivity activity) {
        CheckoutDialog dialog = new CheckoutDialog();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        // Only show a new Dialog if we don't already have one in the manager
        if (fragmentManager.findFragmentByTag(ID) == null) {
            dialog.show(activity.getSupportFragmentManager(), ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_checkout, null, false);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.detail_checkout_dialog_title)
                .setPositiveButton(R.string.detail_checkout_dialog_confirm, (dialog, which) -> checkout())
                .setView(binding.getRoot())
                .setNegativeButton(R.string.detail_checkout_dialog_cancel,
                        (dialog, which) -> dismiss())
                .create();
    }

    private void checkout() {
        Listener listener = (Listener) getActivity();
        listener.checkoutDialogClicked(binding.name.getText().toString());
        dismiss();
    }

    public interface Listener {
        void checkoutDialogClicked(String name);
    }
}
