package com.example.LibManager.services;

public class LengthOfMonth {

    public static Boolean checkExist(int a[], int x) {
        for (int k:a) {
            if(k == x) {
                return true;
            }
        }
        return false;
    }

    public static int LOM(int year, int month) {
        int[] bigMonth = {1, 3, 5, 7, 8, 10, 12};
        int[] smallMonth = { 4, 6, 9, 11};
        if(checkExist(bigMonth, month)) {
            return 31;
        }
        if(checkExist(smallMonth, month)) {
            return 30;
        }
        if(year % 4 == 0 && year % 100 != 0) {
            return 29;
        }
        return 28;
    }
}
