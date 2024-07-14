package com.example.blueoceandive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.bottomsheet.BankPayCodeBottomSheet;
import com.example.blueoceandive.bottomsheet.BankPayCodeBottomSheetActionListener;
import com.example.blueoceandive.bottomsheet.BankTransferBottomSheet;
import com.example.blueoceandive.bottomsheet.BankTransferBottomSheetActionListener;
import com.example.blueoceandive.model.CartItem;
import com.example.blueoceandive.model.CheckoutItem;
import com.example.blueoceandive.model.TripPackage;
import com.example.blueoceandive.util.NumberFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class CheckoutTwoActivity extends AppCompatActivity implements BankTransferBottomSheetActionListener, BankPayCodeBottomSheetActionListener {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private ImageView ivBack;
    private TextView tvSubtotal;
    private Button btnPay;
    private ArrayList<CartItem> cartItems;
    private CheckoutItem checkoutItem;
    private LinearLayout llCarts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_two);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ivBack = findViewById(R.id.iv_back);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        btnPay = findViewById(R.id.btn_pay);
        llCarts = findViewById(R.id.ll_carts);
        ivBack.setOnClickListener(v -> finish());

        if (getIntent() != null) {
            this.cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("carts");
            this.checkoutItem = (CheckoutItem) getIntent().getSerializableExtra("checkoutItem");
            loadView();
        }

        btnPay.setOnClickListener(v -> pay());
    }

    private void loadView() {
        llCarts.removeAllViews();
        cartItems.forEach(cartItem -> {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.item_checkout, null, false);
            ImageView ivPackage = view.findViewById(R.id.iv_package);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            Glide.with(this).load(cartItem.getTripPackage().getImageUrl()).into(ivPackage);
            tvTitle.setText(cartItem.getTripPackage().getPackageName());
            llCarts.addView(view);
        });
        updateTotalPrice(cartItems);
    }

    private void updateTotalPrice(ArrayList<CartItem> carts) {
        AtomicLong subtotal = new AtomicLong(0L);
        carts.forEach(item -> {
            subtotal.addAndGet(item.getSubtotal());
        });
        tvSubtotal.setText(NumberFormat.format(subtotal));
    }

    private void pay() {
//        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
//
//        RadioButton radioButton = (RadioButton) findViewById(selectedId);
//        String paymentMethod = radioButton.getText().toString();

        BankTransferBottomSheet bankTransferBottomSheet = new BankTransferBottomSheet(this);
        bankTransferBottomSheet.show(getSupportFragmentManager(), "BankTransferBottomSheet");
    }

    @Override
    public void onGetPayCode(String paymentMethod) {
        Intent intent = new Intent(this, CheckoutTransferActivity.class);
        intent.putExtra("payCode", getPayCode());
        intent.putExtra("paymentMethod", paymentMethod);
        intent.putExtra("carts", this.cartItems);
        intent.putExtra("checkoutItem", checkoutItem);
        startActivity(intent);
    }

    private String getPayCode() {
        int numberOne = getRandomNumber(1000, 9999);
        int numberTwo = getRandomNumber(1000, 9999);
        int numberThree = getRandomNumber(1000, 9999);
        int numberFour = getRandomNumber(1000, 9999);
        return "" + numberOne + numberTwo + numberThree + numberFour;
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    @Override
    public void onPaymentMethodClicked(String paymentMethod) {
        BankPayCodeBottomSheet bankPayCodeBottomSheet = new BankPayCodeBottomSheet(paymentMethod, this);
        bankPayCodeBottomSheet.show(getSupportFragmentManager(), "BankPayCodeBottomSheet");
    }
}
