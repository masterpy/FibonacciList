# FibonacciList
课程格子笔试题 Fibonacci List

## 一 介绍
一个可以显示斐波那契列表的activity
可以在Android SDK 4.0以上的版本与环境下运行

## 二 功能
列表从0开始，从小到大，每行显示一个斐波那契数字

列表的第n行显示F(n^2)

底部上拉加载更多，默认每次加载20个值

当数值超过10^10时，用科学技术法表示Fibonacci的值

切换顺序：点击按钮会在“从小到大”和“从大到小”排序之间切换

## 三 计算方法
参考http://www.nayuki.io/page/fast-fibonacci-algorithms

Fast doubling 方法：
F(2n) = F(n) * (2*F(n+1) - F(n)).
F(2n+1) = F(n+1)^2 + F(n)^2.

## 四 注意事项
当n越大，计算时间越长，故滑动到后期，所需等待时间越长，请耐心等待。
