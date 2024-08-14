package com.github.karanveerb.logpounce.shizuku

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import rikka.shizuku.Shizuku
import rikka.shizuku.Shizuku.OnRequestPermissionResultListener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.Pattern


class Logcat {
    // This is gemini code (prob should have been chatgpt but whatever)
    private val TAG = "LogcatReader"

    override fun onRequestPermissionsResult(requestCode: Int, grantResult: Int) {
        val granted = grantResult == PackageManager.PERMISSION_GRANTED
        if
                (granted) {
            startReadingLogcat()
        } else {
            // Handle permission denied case (e.g., show a message)
            Log.d(TAG, "Read Logs permission denied")
        }
    }

    private fun startReadingLogcat() {
        val process = try {
            Runtime.getRuntime().exec("logcat -v time -s your_tag")
        } catch (e: Exception) {
            Log.e(TAG, "Error executing logcat command", e)
            return
        }

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val firstPassPattern = Pattern.compile("Zenith_SppRecvThread")
        val secondPassPattern = Pattern.compile("2D 0. 0.")
        // TODO: DESIRED COMMAND "adb logcat Zenith_SppRecvThread*" (potentially replace * with I or whatever log level it is)
        try {
            while (true) {
                val line = reader.readLine() ?: break
                if (compiledPattern.matcher(line).find()) {
                    // Process the filtered log line
                    Log.i(TAG, "Filtered Log: $line") // Example processing
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading logcat", e)
        } finally {
            reader.close()
        }
    }

    fun checkAndRequestPermission(activity: Activity) {
        if (Shizuku.isPreV11()) {
            Log.w(TAG, "Shizuku pre-v11, logcat reading unsupported")
            return
        }

        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            startReadingLogcat()
            return
        }

        Shizuku.addRequestPermissionResultListener(this)
        Shizuku.requestPermission(activity, Shizuku.READ_LOGS)
    }



    /*
    private fun onRequestPermissionsResult(requestCode: Int, grantResult: Int) {
        val granted = grantResult == PackageManager.PERMISSION_GRANTED
        // Do stuff based on the result and the request code
    }

    private val REQUEST_PERMISSION_RESULT_LISTENER =
        OnRequestPermissionResultListener { requestCode: Int, grantResult: Int ->
            this.onRequestPermissionsResult(
                requestCode, grantResult
            )
        }

    protected fun onCreate(savedInstanceState: Bundle?) {
        // ...
        Shizuku.addRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)
        // ...
    }

    protected fun onDestroy() {
        // ...
        Shizuku.removeRequestPermissionResultListener(REQUEST_PERMISSION_RESULT_LISTENER)
        // ...
    }

    private fun checkPermission(code: Int): Boolean {
        if (Shizuku.isPreV11()) {
            // Pre-v11 is unsupported
            return false
        }

        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            // Granted
            return true
        } else if (Shizuku.shouldShowRequestPermissionRationale()) {
            // Users choose "Deny and don't ask again"
            return false
        } else {
            // Request the permission
            Shizuku.requestPermission(code)
            return false
        }
    }
    */
}