package com.example.blueoceandive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blueoceandive.R;
import com.example.blueoceandive.model.CartItem;
import com.example.blueoceandive.model.CheckoutItem;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private ArrayList<CartItem> carts;
    private ImageView ivBack;
    private EditText etFullName, etRegion, etStreetAddress, etPostCode;
    private EditText etCity, etPhone, etEmail;
    private Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        if (getIntent() != null) {
            ArrayList<CartItem> carts = (ArrayList<CartItem>) getIntent().getSerializableExtra("carts");
            this.carts = carts;
        }

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());
        etFullName = findViewById(R.id.et_fullname);
        etRegion = findViewById(R.id.et_region);
        etStreetAddress = findViewById(R.id.et_street_address);
        etPostCode = findViewById(R.id.et_post_code);
        etCity = findViewById(R.id.et_city);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String region = etRegion.getText().toString();
            String streetAddress = etStreetAddress.getText().toString();
            String postCode = etPostCode.getText().toString();
            String city = etCity.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();

            if (fullName.trim().isEmpty() || region.trim().isEmpty() || streetAddress.trim().isEmpty() || postCode.trim().isEmpty() || city.trim().isEmpty() || phone.trim().isEmpty() || email.trim().isEmpty()) {
                Toast.makeText(CheckoutActivity.this, "All field must be filled!", Toast.LENGTH_SHORT).show();
            } else {
                CheckoutItem checkoutItem = new CheckoutItem(
                    fullName,
                    region,
                    streetAddress,
                    postCode,
                    city,
                    phone,
                    email,
                    new ArrayList<>()
                );
                Intent intent = new Intent(CheckoutActivity.this, CheckoutTwoActivity.class);
                intent.putExtra("carts", this.carts);
                intent.putExtra("checkoutItem", checkoutItem);
                startActivity(intent);
            }
        });
    }
}
