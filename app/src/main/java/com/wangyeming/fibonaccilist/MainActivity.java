package com.wangyeming.fibonaccilist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.wangyeming.fibonaccilist.Adapter.FibonacciAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示斐波那契列表的activity
 *
 * @author Yeming Wang
 * @date 2015/03/11
 */
public class MainActivity extends ActionBarActivity {

    //初始化计算的斐波那契数的个数
    private int INIT_THREADHOLD = 100;
    //储存斐波那契列表的List
    private List<BigInteger> fibonacciList = new ArrayList<>();
    //显示斐波那契列表的RecyclerView
    private RecyclerView mRecyclerView;
    //显示斐波那契列表的RecyclerView的显示布局管理
    private RecyclerView.LayoutManager mLayoutManager;
    //Fibonacci Adapter
    private FibonacciAdapter mAdapter;
    private BigInteger number1 = new BigInteger("0");
    private BigInteger number2 = new BigInteger("1");
    //判断当前数是否显示为科学技术法
    private boolean isScientificNotation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayFibonacci(); //设置RecyclerView显示
        calculateFibonacci(); //计算fibonacci数
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
        fibonacciList.add(new BigInteger("0"));
        fibonacciList.add(new BigInteger("1"));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
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

    public void calculateFibonacci() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i=0;i<INIT_THREADHOLD;i++) {
                    getNextNumber(i);
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
    public void getNextNumber(int pos) {
        switch (pos) {
            case 0:
                number1 = new BigInteger("0");
                fibonacciList.add(number1);
                break;
            case 1:
                number2 = new BigInteger("1");
                fibonacciList.add(number2);
                break;
            default:
                BigInteger number = number2.add(number1);
                fibonacciList.add(number);
                number2 = number;
                number1 = number2;
                break;
        }
    }
}
