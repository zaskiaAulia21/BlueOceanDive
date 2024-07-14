package com.example.blueoceandive.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blueoceandive.R;
import com.example.blueoceandive.activity.CheckoutActivity;
import com.example.blueoceandive.adapter.cart.CartActionListener;
import com.example.blueoceandive.adapter.cart.CartAdapter;
import com.example.blueoceandive.model.CartItem;
import com.example.blueoceandive.util.NumberFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FragmentCart extends Fragment implements CartActionListener {

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FragmentContainerDashboardListener listener;
    private ImageView ivBack;
    private CartAdapter cartAdapter;
    private RecyclerView rvCart;
    private ArrayList<CartItem> carts;
    private TextView tvTotalPayment, tvNoItems;
    private Button btnCheckout;
    private ConstraintLayout clTotalPayment;

    public FragmentCart(FragmentContainerDashboardListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = view.findViewById(R.id.iv_back);
        rvCart = view.findViewById(R.id.rv_cart);
        clTotalPayment = view.findViewById(R.id.cl_total_payment);
        cartAdapter = new CartAdapter(this);
        rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCart.setAdapter(cartAdapter);

        tvTotalPayment = view.findViewById(R.id.tv_total_payment);
        tvNoItems = view.findViewById(R.id.tv_no_item);
        btnCheckout = view.findViewById(R.id.btn_checkout);

        btnCheckout.setOnClickListener(v -> {
            checkout();
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        ivBack.setOnClickListener(v -> {
            listener.popBackStack();
        });

        loadData();
    }

    private void loadData() {
        String userId = auth.getUid();
        DatabaseReference dbPost = database.getReference("carts").child(userId);
        dbPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartItem> carts = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    cartItem.setCartId(dataSnapshot.getKey());
                    carts.add(cartItem);
                }
                FragmentCart.this.carts = carts;
                cartAdapter.setData(carts);
                updateTotalPrice(carts);

                if (carts.isEmpty()) {
                    clTotalPayment.setVisibility(View.GONE);
                    tvNoItems.setVisibility(View.VISIBLE);
                } else {
                    clTotalPayment.setVisibility(View.VISIBLE);
                    tvNoItems.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClicked(CartItem cartItem) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.dialog_delete, null, false);
        Button btnDelete = view.findViewById(R.id.btn_delete);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        AlertDialog dialog = alertDialog.create();
        btnDelete.setOnClickListener(v -> {
            deleteCart(cartItem);
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onCheckboxChanged(CartItem cartItem, Boolean isChecked) {
        ArrayList<CartItem> updated = new ArrayList<>(this.carts);
        int index = updated.indexOf(cartItem);
        if (index != -1) {
            updated.get(index).setChecked(isChecked);
            cartAdapter.setData(updated);
        }
        String userId = auth.getUid();
        DatabaseReference dbPost = database.getReference("carts");
        dbPost.child(userId).child(cartItem.getCartId()).setValue(updated.get(index));
        updateTotalPrice(updated);
    }

    private void updateTotalPrice(ArrayList<CartItem> carts) {
        AtomicLong subtotal = new AtomicLong(0L);
        carts.forEach(item -> {
            if (item.getChecked()) {
                subtotal.addAndGet(item.getSubtotal());
            }
        });
        tvTotalPayment.setText(NumberFormat.format(subtotal));
    }

    private void deleteCart(CartItem cartItem) {
        String userId = auth.getUid();
        DatabaseReference dbPost = database.getReference("carts");
        dbPost.child(userId).child(cartItem.getCartId()).removeValue();
        loadData();
    }

    private void checkout() {
        Intent intent = new Intent(requireContext(), CheckoutActivity.class);
        ArrayList<CartItem> updatedCart = new ArrayList<>(
            this.carts
                .stream()
                .filter(cartItem -> cartItem.getChecked())
                .collect(Collectors.toList())
        );
        intent.putExtra("carts", updatedCart);
        startActivity(intent);
    }
}