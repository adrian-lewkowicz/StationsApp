package com.example.stationsapp

import java.util.Calendar
import java.util.Date

object Utils {

    fun checkIfIsCanBeUpdated(date :Date): Boolean {
        ///Todo setup refresh period from config
        if (date != null) {
            val currentTime = Calendar.getInstance().time
            val diff = currentTime.time - date.time
            val hoursDiff = diff / (1000 * 60 * 60)
            return hoursDiff > 24
        }
        return false
    }
}