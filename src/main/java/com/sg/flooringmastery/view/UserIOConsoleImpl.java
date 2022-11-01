/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.flooringmastery.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 *
 * @author Daryl del Rosario
 */
public class UserIOConsoleImpl implements UserIO {
    
    final private Scanner sc = new Scanner(System.in);

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public double readDouble(String msg) {
        while(true) {
            try {
                return Double.parseDouble(this.readString(msg));
            } catch(NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public double readDouble(String msg, double min, double max) {
        double result;
        do {
            result = readDouble(msg);
        } while(result < min || result > max);
        return result;
    }

    @Override
    public float readFloat(String msg) {
        while(true) {
            try {
                return Float.parseFloat(this.readString(msg));
            } catch(NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public float readFloat(String msg, float min, float max) {
        float result;
        do {
            result = readFloat(msg);
        } while(result < min || result > max);
        return result;
    }

    @Override
    public int readInt(String msg) {
        boolean invalidInput = true;
        int num = 0;
        while(invalidInput) {
            try {
                String stringValue = this.readString(msg);
                num = Integer.parseInt(stringValue);
                invalidInput = false;
            } catch(NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    @Override
    public int readInt(String msg, int min, int max) {
        int result;
        do {
            result = readInt(msg);
        } while(result < min  || result > max);
        return result;
    }

    @Override
    public long readLong(String msg) {
        while(true) {
            try {
                return Long.parseLong(this.readString(msg));
            } catch(NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public long readLong(String msg, long min, long max) {
        long result;
        do {
            result = readLong(msg);
        } while(result < min || result > max);
        return result;
    }

    @Override
    public String readString(String msg) {
        System.out.println(msg);
        return sc.nextLine();
    }

    @Override
    public BigDecimal readBigDecimal(String msg) {
        while(true) {
            try{
                String numStr = this.readString(msg);
                return new BigDecimal(numStr).setScale(2, RoundingMode.HALF_UP);
            } catch(Exception e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public BigDecimal readBigDecimal(String msg, BigDecimal min, BigDecimal max) {
        BigDecimal result;
        do {
            result = readBigDecimal(msg);
        } while(result.compareTo(min) < 0 || result.compareTo(max) >0);
        return result;
    }

}
