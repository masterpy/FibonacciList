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
import android.widget.ListView;

import com.wangyeming.fibonaccilist.Adapter.FibonacciAdapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
    private int INIT_THREADHOLD = 30;
    //每次刷新的斐波那契数的个数
    private int REFRASH_THREADHOLD = 10;
    //显示为科学技术法的阈值
    private BigInteger BIG_NUMBER_THREADHOLD = BigInteger.valueOf((long) Math.pow(10, 10));

    //储存斐波那契列表的List
    private List<String> fibonacciList = new ArrayList<>();
    //显示斐波那契列表的RecyclerView
    private RecyclerView mRecyclerView;
    //显示斐波那契列表的RecyclerView的显示布局管理
    private LinearLayoutManager mLayoutManager;
    private ListView lt;
    //Fibonacci Adapter
    private FibonacciAdapter mAdapter;
    //迭代法存储前两个值
    private BigInteger number1;
    private BigInteger number2;
    //公式法的固定值
    private BigDecimal one;
    private BigDecimal two;
    private BigDecimal z;
    private BigDecimal x;
    private BigDecimal y;
    //判断当前数是否显示为科学技术法
    private boolean isScientificNotation = false;
    //判断当前是否在计算
    private boolean isCalculate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayFibonacci(); //设置RecyclerView显示
        calculateBaseNum();
        //newCalculateFibonacci(0, INIT_THREADHOLD); //公式法计算fibonacci数
        calculateFibonacci(0, INIT_THREADHOLD); //迭代法计算fibonacci数
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
            isCalculate = false; //当前正在计算
        }
    };

    /**
     * 调用迭代法获取斐波那契数
     *
     * @param startNum
     */
    public void calculateFibonacci(final int startNum, final int totalNum) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                isCalculate = true; //当前正在计算
                for (int i = startNum; i < startNum + totalNum; i++) {
                    switch (i) {
                        case 0:
                            BigInteger number = getNextNumber(0);
                            fibonacciList.add(number.toString());
                            break;
                        default:
                            String numberDisplay = "";
                            double x = Math.pow(i - 1, 2) + 1;
                            double y = Math.pow(i, 2);
                            int z = (int) (y - x);
                            BigInteger num = new BigInteger("0");
                            //从n^2到（n+1)^2需要计算的次数
                            for (int j = (int) x; j <= y; j++) {
                                num = getNextNumber(j);
                            }
                            Log.d("wym", "z " + z + " num " + num);
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
                            fibonacciList.add(numberDisplay);
                    }
                }
                Message message = Message.obtain();
                message.obj = "ok";
                MainActivity.this.handler1.sendMessage(message);
            }
        }).start();
    }

    public void calculateBaseNum() {
        one = new BigDecimal("1");
        two = new BigDecimal("2");
        z = BigDecimal.valueOf(Math.sqrt(5));
        x = one.add(z).divide(two);
        y = one.subtract(z).divide(two);
    }

    /**
     * 调用公式法获取斐波那契数
     *
     * @param startNum
     */
    public void newCalculateFibonacci(final int startNum, final int totalNum) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                isCalculate = true; //当前正在计算
                for (int i = startNum; i < startNum + totalNum; i++) {
                    BigInteger num = getFibonacciNumber(i);
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
                    Log.d("wym", "n " + i + " numberDisplay " + numberDisplay );
                    fibonacciList.add(numberDisplay);
                }
                Message message = Message.obtain();
                message.obj = "ok";
                MainActivity.this.handler1.sendMessage(message);
            }
        }).start();
    }

    /**
     * 获取下一个斐波那契数值
     *
     * @param pos
     * @return
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


    /**
     * 公式法获取任意n值的斐波那契数
     *
     * @param n
     * @return
     */
    public BigInteger getFibonacciNumber(int n) {
        int nSquare = (int) Math.pow(n, 2);
        BigDecimal number = x.pow(nSquare).subtract(y.pow(nSquare)).divide(z);
        BigInteger num = number.toBigInteger();
        return num;
    }

    public void setScrollerListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
                //判断是否滑到底部
                int lastPos = mLayoutManager.findLastCompletelyVisibleItemPosition() + 1;
                Log.d("wym", "lastPos " + lastPos + " size " + fibonacciList.size());
                if (lastPos > fibonacciList.size() - 2) {
                    Log.d("wym", "底部" + fibonacciList.size());
                    if(!isCalculate) {
                        //newCalculateFibonacci(fibonacciList.size(), REFRASH_THREADHOLD);
                        calculateFibonacci(fibonacciList.size(), REFRASH_THREADHOLD);
                    }
                }
            }
        });
    }

    private static String format(BigInteger x, int scale) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits(scale);
        return formatter.format(x);
    }
}
