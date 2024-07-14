package com.example.blueoceandive.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.blueoceandive.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BankPayCodeBottomSheet extends BottomSheetDialogFragment {

    private BankPayCodeBottomSheetActionListener listener;

    private TextView tvPaymentMethod;
    private Button btnGetPayCode;
    private ImageView ivClose;
    private String paymentMethod;

    public BankPayCodeBottomSheet() {

    }

    public BankPayCodeBottomSheet(String paymentMethod, BankPayCodeBottomSheetActionListener listener) {
        this.listener = listener;
        this.paymentMethod = paymentMethod;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_bank_pay_code, container, false);
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
        btnGetPayCode = view.findViewById(R.id.btn_pay_code);
        tvPaymentMethod = view.findViewById(R.id.tv_payment_method);
        tvPaymentMethod.setText(paymentMethod);

        ivClose.setOnClickListener(v -> {
            dismiss();
        });

        btnGetPayCode.setOnClickListener(v -> {
            listener.onGetPayCode(paymentMethod);
            dismiss();
        });
    }
}
