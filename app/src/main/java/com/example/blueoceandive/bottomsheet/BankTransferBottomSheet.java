package com.example.blueoceandive.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.blueoceandive.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BankTransferBottomSheet extends BottomSheetDialogFragment {

    private BankTransferBottomSheetActionListener listener;

    private ImageView ivClose;
    private FrameLayout flBni, flBca;

    public BankTransferBottomSheet() {

    }

    public BankTransferBottomSheet(BankTransferBottomSheetActionListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_bank_transfer, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog d = (BottomSheetDialog) getDialog();
        if (d != null) {
            d.setOnShowListener(dialog -> {
                View view = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(view);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            });
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivClose = view.findViewById(R.id.iv_close);
        flBca = view.findViewById(R.id.fl_bca);
        flBni = view.findViewById(R.id.fl_bni);

        ivClose.setOnClickListener(v -> dismiss());
        flBni.setOnClickListener(v -> onPaymentMethodClicked("BNI"));
        flBca.setOnClickListener(v -> onPaymentMethodClicked("BCA"));
    }

    private void onPaymentMethodClicked(String paymentMethod) {
        listener.onPaymentMethodClicked(paymentMethod);
        dismiss();
    }
}
