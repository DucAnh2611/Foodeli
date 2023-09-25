package com.example.foodeli.Activities.Cart;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import static androidx.recyclerview.widget.ItemTouchHelper.*;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

public class SwipeItemCartController extends Callback {

    private final RecyclerView.Adapter mAdapter;
    private Context context;

    public SwipeItemCartController(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT );
    }
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
//        mAdapter.deleteItem(position);
    }
}
