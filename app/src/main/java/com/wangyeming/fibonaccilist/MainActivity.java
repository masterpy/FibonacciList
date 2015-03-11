package com.wangyeming.fibonaccilist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.wangyeming.fibonaccilist.Adapter.FibonacciAdapter;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示斐波那契列表的activity
 * 二分法计算斐波那契列表
 *
 * @author Yeming Wang
 * @date 2015/03/11
 */
public class MainActivity extends ActionBarActivity {

    //初始化计算的斐波那契数的个数
    private int INIT_THREADHOLD = 30;
    //每次刷新的斐波那契数的个数
    private int REFRASH_THREADHOLD = 20;
    //显示为科学技术法的阈值
    private BigInteger BIG_NUMBER_THREADHOLD = BigInteger.valueOf((long) Math.pow(10, 10));

    //储存斐波那契列表的List
    private List<String> fibonacciList = new ArrayList<>();
    //显示斐波那契列表的RecyclerView
    private RecyclerView mRecyclerView;
    //显示斐波那契列表的RecyclerView的显示布局管理
    private LinearLayoutManager mLayoutManager;
    //Fibonacci Adapter
    private FibonacciAdapter mAdapter;
    //判断当前数是否显示为科学技术法
    private boolean isScientificNotation = false;
    //判断当前是否在计算
    private boolean isCalculate = false;
    //加载条
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        displayFibonacci(); //设置RecyclerView显示
        fastCalculateFibonacci(0, INIT_THREADHOLD); //计算斐波那契数
        setScrollerListener(); //监听是否滑动到底部
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.reverse) {
            if (mLayoutManager.getReverseLayout()) {
                mLayoutManager.setReverseLayout(false);  //正序排列
            } else {
                mLayoutManager.setReverseLayout(true);  //倒序排列
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示斐波那契列表
     */
    public void displayFibonacci() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FibonacciAdapter(this, fibonacciList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            mAdapter.notifyDataSetChanged();
            isCalculate = false; //计算完成
            progressBar.setVisibility(View.INVISIBLE);
        }
    };

    /**
     * 调用double法获取斐波那契数
     *
     * @param startNum
     * @param totalNum
     */
    public void fastCalculateFibonacci(final int startNum, final int totalNum) {
        progressBar.setVisibility(View.VISIBLE); //显示加载动画
        new Thread(new Runnable() {

            @Override
            public void run() {
                isCalculate = true; //当前正在计算
                for (int i = startNum; i < startNum + totalNum; i++) {
                    BigInteger num = fastFibonacciDoubling((int) Math.pow(i, 2));
                    String numberDisplay = "";
                    if (isScientificNotation) {
                        numberDisplay = format(num, 10);
                    } else {
                        if (num.compareTo(BIG_NUMBER_THREADHOLD) == 1) {
                            //显示为科学技术法
                            numberDisplay = format(num, 10);
                            isScientificNotation = true;
                        } else {
                            numberDisplay = num.toString();
                        }
                    }
                    Log.d("wym", "n " + i + " numberDisplay " + numberDisplay);
                    fibonacciList.add(numberDisplay);
                }
                Message message = Message.obtain();
                message.obj = "ok";
                MainActivity.this.handler1.sendMessage(message);
            }
        }).start();
    }

    /*
     * Fast doubling method.
	 * F(2n) = F(n) * (2*F(n+1) - F(n)).
	 * F(2n+1) = F(n+1)^2 + F(n)^2.
	 */
    private static BigInteger fastFibonacciDoubling(int n) {
        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        int m = 0;
        for (int i = 31 - Integer.numberOfLeadingZeros(n); i >= 0; i--) {
            // a = F(m), b = F(m+1)
            BigInteger d = multiply(a, b.shiftLeft(1).subtract(a));
            BigInteger e = multiply(a, a).add(multiply(b, b));
            a = d;
            b = e;
            m *= 2;
            // 当该位为0时，不加1，当该位为1时，加1
            if (((n >>> i) & 1) != 0) {
                BigInteger c = a.add(b);
                a = b;
                b = c;
                m++;
            }
        }
        return a;
    }

    // 计算相乘
    private static BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    /**
     * 设置滑动监听
     */
    public void setScrollerListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                //判断是否滑到底部
                int lastPos = mLayoutManager.findLastCompletelyVisibleItemPosition() + 1;
                if (lastPos > fibonacciList.size() - 2) {
                    if (!isCalculate) {
                        fastCalculateFibonacci(fibonacciList.size(), INIT_THREADHOLD);
                    }
                }
            }
        });
    }

    /**
     * 科学技术法格式化
     *
     * @param x
     * @param scale
     * @return
     */
    private static String format(BigInteger x, int scale) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }
}
