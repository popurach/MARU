package com.shoebill.maru.ui.feature.mypage.auction

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

@OptIn(DelicateCoroutinesApi::class)
class CountdownViewModel : ViewModel() {
    private val targetTime = getNextSundayMidnight()

    var daysLeft = mutableStateOf(calculateDaysLeft())
    val hoursLeft = mutableStateOf(calculateHoursLeft())
    val minutesLeft = mutableStateOf(calculateMinutesLeft())
    val secondsLeft = mutableStateOf(calculateSecondsLeft())

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                updateTime()
            }
        }
    }

    private fun updateTime() {
        daysLeft.value = calculateDaysLeft()
        hoursLeft.value = calculateHoursLeft()
        minutesLeft.value = calculateMinutesLeft()
        secondsLeft.value = calculateSecondsLeft()
    }

    private fun calculateDaysLeft(): Int =
        TimeUnit.MILLISECONDS.toDays(targetTime - System.currentTimeMillis()).toInt()

    private fun calculateHoursLeft(): Int =
        TimeUnit.MILLISECONDS.toHours(targetTime - System.currentTimeMillis()).toInt() % 24

    private fun calculateMinutesLeft(): Int =
        TimeUnit.MILLISECONDS.toMinutes(targetTime - System.currentTimeMillis()).toInt() % 60

    private fun calculateSecondsLeft(): Int =
        TimeUnit.MILLISECONDS.toSeconds(targetTime - System.currentTimeMillis()).toInt() % 60

    private fun getNextSundayMidnight(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 24)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 7)
        }
        return calendar.timeInMillis
    }
}