package com.wangyeming.fibonaccilist.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangyeming.fibonaccilist.R;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    //显示为科学技术法的阈值
    private BigInteger BIG_NUMBER_THREADHOLD = BigInteger.valueOf((long)Math.pow(10,10));

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
        String numberDisplay = "";
        BigInteger fNum = fibonacciList.get(i);
        if(fNum.compareTo(BIG_NUMBER_THREADHOLD) == 1) {
            //显示为科学技术法
            numberDisplay = format(fNum, 10);
        } else {
            numberDisplay = fNum.toString();
        }
        vh.number.setText(numberDisplay);
    }

    @Override
    public int getItemCount() {
        return fibonacciList.size();
    }

    private static String format(BigInteger x, int scale) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }

}
