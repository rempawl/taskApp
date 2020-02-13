package com.example.taskapp.database.entities

import android.os.Parcelable
import androidx.room.Embedded
import com.example.taskapp.fragments.addReminder.DayOfWeekValue
import com.example.taskapp.fragments.addReminder.ReminderDurationState
import com.example.taskapp.fragments.addReminder.ReminderFrequencyState
import com.example.taskapp.utils.Converters
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate


@Parcelize
data class Frequency(
    val freqState: Int,
    val frequency: Int
) : Parcelable {
    private fun intToDaysOfWeek(): Set<DayOfWeekValue> {
        val days = DayOfWeek.values()
        val result = mutableSetOf<DayOfWeekValue>()
        for (i in 0..days.lastIndex) {
            if (frequency.ushr(i).and(1) == 1) {
                result.add(days[i].value)
            }
        }
        return result
    }

    fun convertToFrequencyState(): ReminderFrequencyState {
        return when (freqState) {
            ReminderFrequencyState.WEEKDAYS_FREQUENCY_INDEX -> ReminderFrequencyState.WeekDays(
                intToDaysOfWeek()
            )
            ReminderFrequencyState.DAILY_FREQUENCY_INDEX -> ReminderFrequencyState.Daily(frequency)
            else -> throw IndexOutOfBoundsException()
        }
    }
}



@Parcelize
data class Duration(
    val durState: Int,
    val duration: Long= 0
) : Parcelable {
    fun convertToDurationState(): ReminderDurationState {
        return when (durState) {
            ReminderDurationState.DAYS_DURATION_INDEX -> ReminderDurationState.DaysDuration(duration.toInt())
            ReminderDurationState.END_DATE_DURATION_INDEX -> {
                ReminderDurationState.EndDate(Converters.getInstance().longToLocalDate(duration))
            }
            ReminderDurationState.NO_END_DATE_DURATION_INDEX -> ReminderDurationState.NoEndDate
            else -> throw IndexOutOfBoundsException()
        }
    }
}

@Parcelize
data class NotificationTime(val hour: Int, val minute: Int, val isSet: Boolean = false) : Parcelable

@Parcelize
//@Entity(tableName = "reminders")
data class Reminder(
//    @PrimaryKey(autoGenerate = true) val reminderID : Long = 0,
//    val taskID : Long,
    val begDate: LocalDate,
    @Embedded val frequency: Frequency,
    @Embedded val duration: Duration,
    @Embedded val notificationTime: NotificationTime,
    val expirationDate : LocalDate,
    val updateDate: LocalDate
) : Parcelable