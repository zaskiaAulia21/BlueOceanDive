package com.example.blueoceandive.adapter.cart;

import com.example.blueoceandive.model.CartItem;

public interface CartActionListener {

    void onDeleteClicked(CartItem cartItem);

    void onCheckboxChanged(CartItem cartItem, Boolean isChecked);
}
