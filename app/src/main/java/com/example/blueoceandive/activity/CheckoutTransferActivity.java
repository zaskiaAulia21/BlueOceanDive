package com.example.blueoceandive.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.blueoceandive.R;
import com.example.blueoceandive.model.CartItem;
import com.example.blueoceandive.model.CheckoutItem;
import com.example.blueoceandive.model.PackageAdditionalService;
import com.example.blueoceandive.model.TourPackage;
import com.example.blueoceandive.util.NumberFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CheckoutTransferActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private ArrayList<CartItem> cartItems;
    private CheckoutItem checkoutItem;
    private String paymentMethod;
    private String payCode;
    private ImageView ivBack;
    private TextView tvTotalPrice;
    private TextView tvPayCode;
    private ImageView ivPaymentMethod, ivCopy;
    private Button btnCompletedTransfer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_transfer);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        ivBack = findViewById(R.id.iv_back);
        ivCopy = findViewById(R.id.iv_copy);
        ivBack.setOnClickListener(v -> {
            finish();
        });
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvPayCode = findViewById(R.id.tv_paycode_value);
        ivPaymentMethod = findViewById(R.id.iv_payment_method);
        btnCompletedTransfer = findViewById(R.id.btn_completed_transfer);

        ivCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Paycode", payCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Paycode has been copied", Toast.LENGTH_SHORT).show();
        });

        btnCompletedTransfer.setOnClickListener(v -> {
            completedTransfer();
        });

        if (getIntent() != null) {
            this.cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("carts");
            this.checkoutItem = (CheckoutItem) getIntent().getSerializableExtra("checkoutItem");
            this.payCode = getIntent().getStringExtra("payCode");
            this.paymentMethod = getIntent().getStringExtra("paymentMethod");
            updateTotalPrice();
            tvPayCode.setText(payCode);
            if (paymentMethod.equals("BNI")) {
                ivPaymentMethod.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bni));
            } else {
                ivPaymentMethod.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bca));
            }
        }
    }

    private void updateTotalPrice() {
        AtomicLong subtotal = new AtomicLong(0L);
        this.cartItems.forEach(item -> {
            subtotal.addAndGet(item.getSubtotal());
        });
        tvTotalPrice.setText(NumberFormat.format(subtotal));
    }

    private void completedTransfer() {
        String userId = auth.getUid();
        DatabaseReference dbProcess = database.getReference("processes");
        DatabaseReference cartRef = dbProcess.child(userId).push();
        this.checkoutItem.setCarts(cartItems);
        cartRef.setValue(this.checkoutItem);
        DatabaseReference dbCart = database.getReference("carts");
        this.cartItems.forEach(item -> {
            dbCart.child(userId).child(item.getCartId()).removeValue();
        });

        Intent intent = new Intent(this, CheckoutBarcodeActivity.class);
        startActivity(intent);
    }
}
