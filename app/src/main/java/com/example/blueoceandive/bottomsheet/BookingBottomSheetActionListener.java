package com.example.blueoceandive.bottomsheet;

import com.example.blueoceandive.model.CartItem;

import java.util.ArrayList;

public interface BookingBottomSheetActionListener {

    void onCheckout(ArrayList<CartItem> carts);
}
