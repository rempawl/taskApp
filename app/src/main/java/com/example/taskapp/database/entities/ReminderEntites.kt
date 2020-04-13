package com.example.taskapp.database.entities

import android.os.Parcelable
import androidx.room.Embedded
import com.example.taskapp.utils.reminder.DayOfWeekValue
import com.example.taskapp.utils.reminder.ReminderDurationState
import com.example.taskapp.utils.reminder.ReminderFrequencyState
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime


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

    fun getUpdateDate(lastRealizationDate : LocalDate) =
        this.convertToFrequencyState().calculateRealizationDate(lastRealizationDate)

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
    val duration: Long= 0 //when durState is NoEndDate then duration is 0
) : Parcelable {
    fun convertToDurationState(): ReminderDurationState {
        return when (durState) {
            ReminderDurationState.DAYS_DURATION_INDEX -> ReminderDurationState.DaysDuration(duration.toInt())
            ReminderDurationState.END_DATE_DURATION_INDEX -> {
                ReminderDurationState.EndDate(LocalDate.ofEpochDay(duration))
            }
            ReminderDurationState.NO_END_DATE_DURATION_INDEX -> ReminderDurationState.NoEndDate
            else -> throw IndexOutOfBoundsException()
        }
    }
}

@Parcelize
data class  NotificationTime(val hour: Int, val minute: Int, val isSet: Boolean = false) : Parcelable{
    fun convertToLocalTime() : LocalTime = LocalTime.of(hour, minute)

    companion object{
        fun from(time: LocalTime,isSet: Boolean = true) = NotificationTime(time.hour,time.minute,isSet)

    }

}

@Parcelize
data class Reminder(
    val begDate: LocalDate,
    @Embedded val frequency: Frequency,
    @Embedded val duration: Duration,
    @Embedded val notificationTime: NotificationTime,
    val expirationDate : LocalDate,
    val realizationDate: LocalDate
) : Parcelable{

    /**
     * returns this if realization date has not been updated
     * else new instance with updated realization date
     */
    fun updateRealizationDate() : Reminder {
        val date = frequency.getUpdateDate(lastRealizationDate = realizationDate)
        return if(date != realizationDate){
            this.copy(realizationDate = date)
        }else{
            this
        }
    }


}