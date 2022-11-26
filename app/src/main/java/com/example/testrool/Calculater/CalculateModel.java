package com.example.testrool.Calculater;

import static java.lang.Math.abs;
import android.util.Log;

import com.example.testrool.bean.Model;

public class CalculateModel {
/*    //灰度值
    Double[] x={46.46566667,44.11233333,42.57,40.865,35.34566667,28.933,22.196,18.624,12.293};
    //浓度值
    Double[] y={0.1,0.5,1.0,2.0,5.0,8.0,10.0,12.0,15.0};

    int num=9;*/

    public static Model getModel(Double[] x, Double[] y, int num){
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
/*        if ( abs(denominator) <=  1e-15)
        {
            return null;
        }*/
        double a = (num * sum_xy - sum_x * sum_y) / denominator;
        double b = (sum_xsquared * sum_y - sum_x * sum_xy) / denominator;

        Model model = new Model();
        model.setA(a);
        model.setB(b);
        return model;
    }
}
