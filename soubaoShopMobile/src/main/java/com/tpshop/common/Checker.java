package com.tpshop.common;

public class Checker {

    static {
        try {
            System.loadLibrary("curl");
            System.loadLibrary("SPMobile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static native boolean Init();

    public static native int Check(String header, String url);

    public static native void Finished();

}
