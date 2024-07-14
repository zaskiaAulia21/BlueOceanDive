package com.example.blueoceandive.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ImageCardDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public ImageCardDecoration(Context context) {
        margin = 8;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        if (spanIndex == 0) {
            outRect.left = 2 * margin;
            outRect.right = margin;
        } else {
            outRect.left = margin;
            outRect.right = 2 * margin;
        }
        outRect.top = 2 * margin;
    }
}