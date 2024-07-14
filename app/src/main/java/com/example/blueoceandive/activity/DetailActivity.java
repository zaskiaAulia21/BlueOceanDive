package com.example.blueoceandive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.example.blueoceandive.R;
import com.example.blueoceandive.bottomsheet.BookingBottomSheet;
import com.example.blueoceandive.bottomsheet.BookingBottomSheetActionListener;
import com.example.blueoceandive.model.CartItem;
import com.example.blueoceandive.model.TripPackage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements BookingBottomSheetActionListener {

    private FirebaseDatabase database;

    private ImageView ivHeader, ivBack;
    private TextView tvTitle, tvPrice, tvPriceDescription, tvDuration;
    private TextView tvLocation, tvDescription, tvInclude, tvNote;
    private TextView tvTotalPayment;
    private Button btnBook;
    private LinearLayout llReview;

    private TripPackage tripPackage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_package);

        database = FirebaseDatabase.getInstance();

        ivHeader = findViewById(R.id.iv_header);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());
        tvTitle = findViewById(R.id.tv_title);
        tvPrice = findViewById(R.id.tv_price);
        tvPriceDescription = findViewById(R.id.tv_price_description);
        tvDuration = findViewById(R.id.tv_duration);
        tvLocation = findViewById(R.id.tv_location);
        tvDescription = findViewById(R.id.tv_description);
        tvInclude = findViewById(R.id.tv_include);
        tvNote = findViewById(R.id.tv_note);
        tvTotalPayment = findViewById(R.id.tv_total_payment);
        llReview = findViewById(R.id.ll_review);
        btnBook = findViewById(R.id.btn_book);
        btnBook.setOnClickListener(v -> book());

        if (getIntent() != null) {
            String packageId = getIntent().getStringExtra("packageId");
            loadData(packageId);
        }
    }

    private void loadData(String packageId) {
        DatabaseReference db = database.getReference("packages");
        db.child(packageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TripPackage tripPackage = snapshot.getValue(TripPackage.class);
                tripPackage.setId(snapshot.getKey());
                DetailActivity.this.tripPackage = tripPackage;
                Glide.with(DetailActivity.this).load(tripPackage.getImageUrl()).into(ivHeader);
                tvTitle.setText(tripPackage.getPackageName());
                tvPrice.setText(tripPackage.getFormattedPrice());
                tvPriceDescription.setText(tripPackage.getPriceDescription());
                tvDuration.setText(tripPackage.getFormattedDuration());
                tvLocation.setText(tripPackage.getLocation());
                tvDescription.setText(tripPackage.getPackageDescription());
                tvInclude.setText(tripPackage.getInclude());
                tvNote.setText(tripPackage.getNote() + tripPackage.getNote() + tripPackage.getNote());
                tvTotalPayment.setText(tripPackage.getFormattedPrice());
                for (int i = 0; i < tripPackage.getRating(); i++) {
                    ImageView imageView = new ImageView(DetailActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(48, 48);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setBackgroundResource(R.drawable.ic_star);
                    llReview.addView(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCheckout(ArrayList<CartItem> carts) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("carts", carts);
        startActivity(intent);
    }

    private void book() {
        BookingBottomSheet bookingBottomSheet = new BookingBottomSheet(
                tripPackage,
                this
        );
        bookingBottomSheet.show(getSupportFragmentManager(), "BookingBottomSheet");
    }
}
