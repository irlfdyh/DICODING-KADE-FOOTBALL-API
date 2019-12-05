package com.android.footballapi.util

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat

class DateKtTest {

    @Test
    fun toSimpleDateString() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.parse("02/28/2018")
        assertEquals("Wed, 28 Feb 2018", toSimpleDateString(date))
    }
}