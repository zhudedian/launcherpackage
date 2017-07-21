package com.ider.launcherpackage.clean;

import android.util.Log;

import java.io.IOException;


/**
 * Created by Eric on 2017/7/20.
 */

public class Hjjkl {

    private static void execCommand(String command) {
        try {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec(command);


            if(proc.waitFor() != 0) {
                Log.i("ProcessManager", "execCommand: exit value = " + proc.exitValue());
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }
}
