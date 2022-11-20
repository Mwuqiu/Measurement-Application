package com.example.testrool.Calculater;

import static java.lang.Math.abs;
import android.util.Log;

public class CalculateModel {
    //灰度值
    double[] x={46.46566667,44.11233333,42.57,40.865,35.34566667,28.933,22.196,18.624,12.293};
    //浓度值
    double[] y={0.1,0.5,1,2,5,8,10,12,15};
    int num=9;

    CalculateModel(){}

    CalculateModel(double[] x, double[] y,int num){
        this.x = x;
        this.y = y;
        this.num = num;
    }

    public String get_Model(){
        String str=getmodel(x,y,num);
        Log.v("aaa",str);//控制台输出拟合方程
        return str;
    }

    private String getmodel(double[] x, double[] y, int num){
        double denominator = 0.0;
        double sum_xsquared = 0.0;
        double sum_y = 0.0;
        double sum_x = 0.0;
        double sum_xy = 0.0;
        for (int i = 0; i < num; ++i)
        {
            sum_xsquared += x[i] * x[i];
            sum_y += y[i];
            sum_x += x[i];
            sum_xy += x[i] * y[i];
        }
        denominator = (num * sum_xsquared - sum_x * sum_x);
        if ( abs(denominator) <=  1e-15)
        {
            return null;
        }
        double a = (num * sum_xy - sum_x * sum_y) / denominator;
        double b = (sum_xsquared * sum_y - sum_x * sum_xy) / denominator;
        String str="y="+a+"*x+"+b;
        Log.e("aaa","111");
        return str;
    }
}
