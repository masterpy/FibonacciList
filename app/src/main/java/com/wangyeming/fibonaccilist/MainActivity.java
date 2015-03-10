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

import com.wangyeming.fibonaccilist.Adapter.FibonacciAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示斐波那契列表的activity
 * 迭代法计算斐波那契列表
 *
 * @author Yeming Wang
 * @date 2015/03/11
 */
public class MainActivity extends ActionBarActivity {

    //初始化计算的斐波那契数的个数
    private int INIT_THREADHOLD = 20;
    //每次刷新的斐波那契数的个数
    private int REFRASH_THREADHOLD = 20;
    //储存斐波那契列表的List
    private List<BigInteger> fibonacciList = new ArrayList<>();
    //显示斐波那契列表的RecyclerView
    private RecyclerView mRecyclerView;
    //显示斐波那契列表的RecyclerView的显示布局管理
    private LinearLayoutManager mLayoutManager;
    //Fibonacci Adapter
    private FibonacciAdapter mAdapter;
    private BigInteger number1;
    private BigInteger number2;
    //判断当前数是否显示为科学技术法
    private boolean isScientificNotation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayFibonacci(); //设置RecyclerView显示
        calculateFibonacci(0); //计算fibonacci数
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
        }
    };

    public void calculateFibonacci(final int startNum) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //每次加载30个值
                for (int i = startNum; i < startNum + INIT_THREADHOLD; i++) {
                    Log.d("wym", "ssssssss " + i);
                    switch (i) {
                        case 0:
                            BigInteger number = getNextNumber(0);
                            fibonacciList.add(number);
                            break;
                        default:
                            double x = Math.pow(i - 1, 2) + 1 ;
                            double y = Math.pow(i, 2);
                            int z = (int) (y - x);
                            BigInteger num = new BigInteger("0");
                            //从n^2到（n+1)^2需要计算的次数
                            for (int j = (int) x; j <= y; j++) {
                                num = getNextNumber(j);
                            }
                            Log.d("wym", "z " + z + " num " + num);
                            fibonacciList.add(num);
                    }
                }
                Message message = Message.obtain();
                message.obj = "ok";
                MainActivity.this.handler1.sendMessage(message);
            }
        }).start();
    }

    /**
     * 获取下一个斐波那契数值
     */
    public BigInteger getNextNumber(int pos) {
        switch (pos) {
            case 0:
                number1 = new BigInteger("0");
                //fibonacciList.add(number1);
                return number1;
            case 1:
                number2 = new BigInteger("1");
                //fibonacciList.add(number2);
                return number2;
            default:
                BigInteger number = number2.add(number1);
                //fibonacciList.add(number);
                number2 = number;
                number1 = number2;
                return number;
        }
    }


    /*public void getFibonacciNumber(int n) {
        int nSquare = (int)Math.pow(n,2);
        switch (nSquare) {
            case 0:
                fibonacciList.add(new BigInteger("0"));
                break;
            default:
                BigInteger one = new BigInteger("1");
                BigInteger two = new BigInteger("2");
                BigInteger z = BigInteger.valueOf((long)Math.sqrt(5));
                BigInteger x = one.add(z).divide(two);
                BigInteger y = one.subtract(z).divide(two);
                BigInteger number = x.pow(nSquare).subtract(y.pow(nSquare)).divide(z);
                System.out.println((long)Math.sqrt(5));
                Log.d("wym", "one " + one + " two " + two + " z " + z + " x " + x + " y " + y + " num " + number);
                fibonacciList.add(number);
                break;
        }
    }*/

    public void setScrollerListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                //判断是否滑到底部
                int lastPos = mLayoutManager.findLastCompletelyVisibleItemPosition() + 1;
                Log.d("wym", "lastPos " + lastPos + " size " + fibonacciList.size());
                if (lastPos > fibonacciList.size() - 2) {
                    Log.d("wym", "底部");
                    calculateFibonacci(fibonacciList.size());
                }
            }
        });
    }
}
