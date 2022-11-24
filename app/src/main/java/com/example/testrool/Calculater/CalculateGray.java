package com.example.testrool.Calculater;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Arrays;

class RGB {
    int red, green, blue, alpha;

    RGB(int r, int g, int b, int a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    String Print() {
        return String.format("[%3d,%3d,%3d]", red, green, blue);
    }
}

public class CalculateGray {

    final static int averageMode = 1;
    final static int medianMode = 2;

    public double getGray(Bitmap bitmap, int mode) {
        double result = 0;
        int height = bitmap.getHeight();//图片的高度
        int width = bitmap.getWidth();//图片的宽度

        RGB imgRGB[][] = new RGB[width][height]; // 存图片的rgb
        double gray[][] = new double[width][height]; // 存图片的灰度值
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = bitmap.getPixel(i, j);
                //获取像素点（i, j）的RGB值
                imgRGB[i][j] = new RGB(Color.red(pixel), Color.green(pixel), Color.blue(pixel), Color.alpha(pixel));
                //转灰度值
                gray[i][j] = imgRGB[i][j].red * 0.299 + imgRGB[i][j].green * 0.587 + imgRGB[i][j].blue * 0.114;
                //打印信息
                //Log.e("pictureInfoShow", imgRGB[i][j].Print() + "  " + gray[i][j]);
            }
        }
        switch (mode) {
            case averageMode:
                double sum = 0;
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        sum += gray[i][j];
                    }
                }
                result = sum / (width * height);
                break;
            case medianMode:
                double temp[] = new double[width * height + 2];
                int index = -1;
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        temp[++index] = gray[i][j];
                    }
                }
                Arrays.sort(temp);
                if (temp.length % 2 == 0) {
                    result = (temp[temp.length / 2 - 1] + temp[temp.length / 2]) / 2;
                } else {
                    result = temp[temp.length / 2];
                }
                break;
            default:
                break;
        }
        return result;
    }
}

