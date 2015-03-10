package com.wangyeming.fibonaccilist.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangyeming.fibonaccilist.R;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Fibonacci Adapter for RecyclerView
 * @author Wang
 * @data 2015/3/11
 */
public class FibonacciAdapter extends RecyclerView.Adapter<FibonacciAdapter.ViewHolder> {

    private List<BigInteger> fibonacciList = new ArrayList<>();
    private LayoutInflater mInflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public FibonacciAdapter(Context context, List<BigInteger> fibonacciList) {
        mInflater = LayoutInflater.from(context);
        this.fibonacciList = fibonacciList;
    }

    @Override
    public FibonacciAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_fibonacci,
                viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        vh.number = (TextView) view.findViewById(R.id.number);
        return vh;
    }

    @Override
    public void onBindViewHolder(FibonacciAdapter.ViewHolder vh, int i) {
        vh.number.setText(fibonacciList.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return fibonacciList.size();
    }

}
