package com.stefanpetcu.api.testingharness.hammingsproblem.support

import java.text.SimpleDateFormat
import java.util.Calendar

object DateUtils {
    fun currentDateTime(): String {
        val formatter = SimpleDateFormat("dd/M/yyyy hh:mm:ss:SSS")

        return formatter.format(Calendar.getInstance().time)
    }
}
