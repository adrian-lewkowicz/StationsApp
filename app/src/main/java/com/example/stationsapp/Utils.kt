package com.example.stationsapp

import java.util.Calendar
import java.util.Date

object Utils {

    fun checkIfIsCanBeUpdated(date :Date?): Boolean {
        if (date != null) {
            val currentTime = Calendar.getInstance().time
            val diff = currentTime.time - date.time
            val hoursDiff = diff / (1000 * 60 * 60)
            return hoursDiff > 24
        }
        return false
    }

    fun textNormalizer(textToNormal: String) : String{
        val normalizedText = textToNormal.replace("[^\\p{ASCII}]".toRegex(),
            transform = { result ->
                when (result.value) {
                    "ł", "Ł" -> "l"
                    "ą", "Ą" -> "a"
                    "ć", "Ć" -> "c"
                    "ę", "Ę" -> "e"
                    "ń", "Ń" -> "n"
                    "ó", "Ó" -> "o"
                    "ś", "Ś" -> "s"
                    "ż", "Ż" -> "z"
                    "ź", "Ź" -> "z"
                    else -> result.value
                }
            })
        return normalizedText
    }
}