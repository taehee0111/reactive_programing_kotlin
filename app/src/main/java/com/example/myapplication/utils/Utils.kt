package com.example.myapplication.utils

import android.util.Log
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

object Utils {
    private val tag: String = Utils::class.java.simpleName

     fun longRunningTsk(): Long {
        val time = measureTimeMillis {
            Log.d(tag,"Please wait")

            Thread.sleep(2000)
            Log.d(tag,"Delay Over")
        }
        return time
    }
}