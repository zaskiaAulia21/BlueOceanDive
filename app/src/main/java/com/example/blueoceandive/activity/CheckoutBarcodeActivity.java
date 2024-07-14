package com.example.blueoceandive.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blueoceandive.R;

import java.util.Random;

public class CheckoutBarcodeActivity extends AppCompatActivity {

    private ImageView ivBack, ivBarcode, ivCopy;
    private TextView tvTicketCode;

    private String ticketCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_barcode);

        ivBack = findViewById(R.id.iv_back);
        ivBarcode = findViewById(R.id.iv_barcode);
        ivCopy = findViewById(R.id.iv_copy);
        tvTicketCode = findViewById(R.id.tv_ticket_code_value);

        ticketCode = getTicketCode();
        tvTicketCode.setText(ticketCode);

        ivBack.setOnClickListener(v -> finish());
        ivCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Ticket Code", ticketCode);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Ticket Code has been copied", Toast.LENGTH_SHORT).show();
        });
    }

    private String getTicketCode() {
        int numberOne = getRandomNumber(1000, 9999);
        int numberTwo = getRandomNumber(1000, 9999);
        int numberThree = getRandomNumber(1000, 9999);
        int numberFour = getRandomNumber(1000, 9999);
        return "" + numberOne + numberTwo + numberThree + numberFour;
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }
}
