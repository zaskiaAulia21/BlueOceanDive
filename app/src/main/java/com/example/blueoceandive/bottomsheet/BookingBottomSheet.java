package com.example.blueoceandive.bottomsheet;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.blueoceandive.R;
import com.example.blueoceandive.adapter.spinner.AdditionalShuttleServiceArrayAdapter;
import com.example.blueoceandive.adapter.spinner.TourPackageArrayAdapter;
import com.example.blueoceandive.model.CartItem;
import com.example.blueoceandive.model.PackageAdditionalService;
import com.example.blueoceandive.model.TourPackage;
import com.example.blueoceandive.model.TripPackage;
import com.example.blueoceandive.util.NumberFormat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class BookingBottomSheet extends BottomSheetDialogFragment {

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private Spinner spinnerTourPackage, spinnerAdditionalShuttleService;
    private LinearLayout llDatePicker, llOrder;

    private TextView tvDate;
    private ImageView ivDate, ivChevron;
    private Button btnBookNow, btnAddToCart, btnCheckout;
    private ProgressBar loading;
    private TripPackage tripPackage;
    private BookingBottomSheetActionListener listener;
    private Long grandTotalPrice;

    public BookingBottomSheet() {

    }

    public BookingBottomSheet(TripPackage tripPackage, BookingBottomSheetActionListener listener) {
        this.tripPackage = tripPackage;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_booking, container, false);
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

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        spinnerTourPackage = view.findViewById(R.id.spinner_tour_package);
        spinnerAdditionalShuttleService = view.findViewById(R.id.spinner_additional_shuttle_service);
        llDatePicker = view.findViewById(R.id.ll_date_picker);
        llOrder = view.findViewById(R.id.ll_order);
        tvDate = view.findViewById(R.id.tv_date);
        ivDate = view.findViewById(R.id.iv_date);
        ivChevron = view.findViewById(R.id.iv_chevron);
        btnBookNow = view.findViewById(R.id.btn_book_now);
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart);
        btnCheckout = view.findViewById(R.id.btn_checkout);
        loading = view.findViewById(R.id.loading);

        TourPackageArrayAdapter adapter1 = new TourPackageArrayAdapter(requireActivity(), R.layout.app_spinner_item, R.id.tv_title, tripPackage.getTourPackages());
        spinnerTourPackage.setAdapter(adapter1);
        spinnerTourPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateOrderView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<PackageAdditionalService> packageAdditionalServices = new ArrayList<>();
        packageAdditionalServices.add(new PackageAdditionalService("", "Tidak ada", 0));
        packageAdditionalServices.addAll(tripPackage.getAdditionalShuttleService());
        AdditionalShuttleServiceArrayAdapter adapter2 = new AdditionalShuttleServiceArrayAdapter(requireActivity(), R.layout.app_spinner_item, R.id.tv_title, packageAdditionalServices);
        spinnerAdditionalShuttleService.setAdapter(adapter2);
        spinnerAdditionalShuttleService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateOrderView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        llDatePicker.setOnClickListener(v -> {
            final Calendar newCalendar = Calendar.getInstance();
            final DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view1, year, monthOfYear, dayOfMonth) -> {
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String selectedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(mCalendar.getTime());
                tvDate.setText(selectedDate);
                tvDate.setTextColor(getResources().getColor(R.color.black, null));
                ivDate.setColorFilter(getResources().getColor(R.color.black, null));
                ivChevron.setColorFilter(getResources().getColor(R.color.black, null));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnBookNow.setOnClickListener(v -> bookNow());
        btnAddToCart.setOnClickListener(v -> addToCart());
        btnCheckout.setOnClickListener(v -> checkout());
    }

    private void updateOrderView() {
        llOrder.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());

        TourPackage tourPackage = (TourPackage) spinnerTourPackage.getSelectedItem();
        View orderItemOne = layoutInflater.inflate(R.layout.item_order, null, false);
        TextView tvPriceOne = orderItemOne.findViewById(R.id.tv_price);
        tvPriceOne.setText(NumberFormat.format(tourPackage.getPrice()));

        PackageAdditionalService packageAdditionalService = (PackageAdditionalService) spinnerAdditionalShuttleService.getSelectedItem();
        View orderItemTwo = layoutInflater.inflate(R.layout.item_order, null, false);
        TextView tvPriceTwo = orderItemTwo.findViewById(R.id.tv_price);
        tvPriceTwo.setText(NumberFormat.format(packageAdditionalService.getPrice()));

        Long grandTotalPrice = Long.valueOf(tourPackage.getPrice()) + Long.valueOf(packageAdditionalService.getPrice());
        View grandTotal = layoutInflater.inflate(R.layout.item_grand_total, null, false);
        TextView tvPriceGrandTotal = grandTotal.findViewById(R.id.tv_price);
        tvPriceGrandTotal.setText(NumberFormat.format(grandTotalPrice));
        this.grandTotalPrice = grandTotalPrice;

        llOrder.addView(orderItemOne);
        llOrder.addView(orderItemTwo);
        llOrder.addView(grandTotal);
    }

    private void bookNow() {
        String date = tvDate.getText().toString();
        TourPackage tourPackage = (TourPackage) spinnerTourPackage.getSelectedItem();
        PackageAdditionalService packageAdditionalService = (PackageAdditionalService) spinnerAdditionalShuttleService.getSelectedItem();
        if (date.trim().isEmpty() || date.equalsIgnoreCase("date") || packageAdditionalService == null || tourPackage == null) {
            Toast.makeText(requireContext(), "All field must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        updateOrderView();

        llOrder.setVisibility(View.VISIBLE);
        btnBookNow.setVisibility(View.GONE);
        btnCheckout.setVisibility(View.VISIBLE);
        btnAddToCart.setVisibility(View.VISIBLE);
    }

    private void addToCart() {
        loading.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference("carts");
        String id = auth.getUid();
        DatabaseReference cartRef = myRef.child(id).push();
        TourPackage tourPackage = (TourPackage) spinnerTourPackage.getSelectedItem();
        PackageAdditionalService packageAdditionalService = (PackageAdditionalService) spinnerAdditionalShuttleService.getSelectedItem();
        cartRef.setValue(
                new CartItem(
                        UUID.randomUUID().toString(),
                        this.tripPackage,
                        tvDate.getText().toString(),
                        tourPackage,
                        packageAdditionalService,
                        grandTotalPrice,
                        false
                )
        );
        loading.setVisibility(View.GONE);
        Toast.makeText(requireContext(), "Add to cart success!", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void checkout() {
        loading.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference("carts");
        String id = auth.getUid();
        DatabaseReference cartRef = myRef.child(id).push();
        TourPackage tourPackage = (TourPackage) spinnerTourPackage.getSelectedItem();
        PackageAdditionalService packageAdditionalService = (PackageAdditionalService) spinnerAdditionalShuttleService.getSelectedItem();
        CartItem cartItem = new CartItem(
                UUID.randomUUID().toString(),
                this.tripPackage,
                tvDate.getText().toString(),
                tourPackage,
                packageAdditionalService,
                grandTotalPrice,
                false
        );
        cartRef.setValue(cartItem);
        loading.setVisibility(View.GONE);
        ArrayList<CartItem> carts = new ArrayList<>();
        carts.add(cartItem);
        listener.onCheckout(carts);
    }
}
