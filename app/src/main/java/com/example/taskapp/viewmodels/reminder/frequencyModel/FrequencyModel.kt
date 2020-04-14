package com.example.taskapp.viewmodels.reminder.frequencyModel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.taskapp.BR
import com.example.taskapp.database.entities.Frequency
import com.example.taskapp.utils.reminder.DayOfWeekValue
import com.example.taskapp.utils.reminder.ReminderFrequencyState
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import org.threeten.bp.LocalDate

class DefaultFrequencyModel @AssistedInject constructor(@Assisted frequency: Frequency?) :
    FrequencyModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(frequency: Frequency? = null): FrequencyModel
    }

    override var frequencyState: ReminderFrequencyState = ReminderFrequencyState.Daily()
        private set
    private val isEdited: Boolean

    @Bindable
    override var currentDailyFrequency = ReminderFrequencyState.INITIAL_FREQUENCY
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentDailyFrequency)
        }

    override var currentWeekDays: Set<DayOfWeekValue> = ReminderFrequencyState.WeekDays().daysOfWeek


    init {
        if (frequency != null) {
            isEdited = true
            when (val freqState = frequency.convertToFrequencyState()) {
                is ReminderFrequencyState.Daily -> setDailyFrequency(freq = freqState.frequency)
                is ReminderFrequencyState.WeekDays -> setDaysOfWeekFrequency(daysOfWeek = freqState.daysOfWeek)
            }
        } else {
            isEdited = false
        }
    }

    override fun setDailyFrequency(freq: Int ) {
        frequencyState = ReminderFrequencyState.Daily(freq)
        currentDailyFrequency = freq
    }


    override fun setDaysOfWeekFrequency(daysOfWeek: Set<DayOfWeekValue> ) {
        currentWeekDays = daysOfWeek
        frequencyState = ReminderFrequencyState.WeekDays(daysOfWeek)
    }

    override fun getUpdateDate(begDate: LocalDate) = frequencyState.calculateRealizationDate(
        lastRealizationDate = begDate,
        isBeginning = !isEdited
    )


}

abstract class FrequencyModel :
    BaseObservable() {

    abstract val frequencyState: ReminderFrequencyState

    abstract var currentDailyFrequency: Int

    abstract var currentWeekDays: Set<DayOfWeekValue>

    abstract fun setDailyFrequency(freq: Int = currentDailyFrequency)

    abstract fun setDaysOfWeekFrequency(daysOfWeek: Set<DayOfWeekValue> = currentWeekDays)

    fun getFrequency(): Frequency = frequencyState.convertToFrequency()

    abstract fun getUpdateDate(begDate: LocalDate): LocalDate

}