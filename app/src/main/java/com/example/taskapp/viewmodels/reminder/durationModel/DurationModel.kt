package com.example.taskapp.viewmodels.reminder.durationModel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.example.taskapp.MyApp.Companion.TODAY
import com.example.taskapp.database.entities.Duration
import com.example.taskapp.utils.reminder.ReminderDurationState
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import org.threeten.bp.LocalDate

class DefaultDurationModel @AssistedInject constructor(
    @Assisted duration: Duration?,
    @Assisted begDate: LocalDate
) :
    DurationModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(
            duration: Duration? = null,
            begDate: LocalDate = LocalDate.now()
        ): DefaultDurationModel
    }

    //Today for new tasks begDate for edited tasks
    private val validationDate: LocalDate = begDate

    override var durationState: ReminderDurationState = ReminderDurationState.NoEndDate
        private set(value) {
            field = value
            notifyPropertyChanged(BR.dateValid)
        }

    @Bindable
    override var currentDaysDuration = 10
        private set(value) {
            field = value
            notifyPropertyChanged(BR.currentDaysDuration)
        }

    @Bindable
    override var beginningDate = validationDate
        set(value) {
            if (isBeginningDateValid(value)) {
                field = value
                notifyPropertyChanged(BR.beginningDate)
                begDateError.set(false)
            } else {
                begDateError.set(true)
            }
        }

    @Bindable
    override var currentEndDate: LocalDate = LocalDate.ofYearDay(TODAY.year, TODAY.dayOfYear + 10)
        private set(value) {
            if (isEndDateValid(value)) {
                field = value
                notifyPropertyChanged(BR.currentEndDate)
                endDateError.set(false)
            } else {
                endDateError.set(true)
            }

        }

    init {
        if (duration != null) {
            when (val durState = duration.convertToDurationState()) {
                is ReminderDurationState.NoEndDate -> {
                    setNoEndDateDurationState()
                }
                is ReminderDurationState.DaysDuration -> setDaysDurationState(days = durState.days)
                is ReminderDurationState.EndDate -> setEndDateDurationState(endDate = durState.date)
            }

        }
    }

    @Bindable
    override fun isDateValid(): Boolean {
        return if (durationState is ReminderDurationState.EndDate) {
            isEndDateValid()
        } else {
            true
        }
    }

    override fun isEndDateValid(date: LocalDate) =
        !date.isBefore(beginningDate) && !date.isBefore(LocalDate.now())


    override fun isBeginningDateValid(date: LocalDate): Boolean {
        val isValid = !date.isBefore(validationDate)
        return if (durationState is ReminderDurationState.EndDate) {
            isValid && date.isBefore(currentEndDate)
        } else {
            isValid
        }
    }


    override fun setNoEndDateDurationState() {
        durationState = ReminderDurationState.NoEndDate
    }

    override fun setDaysDurationState(days: Int) {
        durationState = ReminderDurationState.DaysDuration(days)
        currentDaysDuration = days
    }

    override fun setEndDateDurationState(endDate: LocalDate) {
        durationState = ReminderDurationState.EndDate(endDate)
        currentEndDate = endDate
    }


}

abstract class DurationModel : BaseObservable() {


    abstract val durationState: ReminderDurationState

    //true when error message should be displayed
    val begDateError = ObservableField<Boolean>(false)
    val endDateError = ObservableField<Boolean>(false)

    abstract val currentDaysDuration: Int

    abstract val beginningDate: LocalDate

    abstract val currentEndDate: LocalDate


    abstract fun isDateValid(): Boolean

    protected abstract fun isEndDateValid(date: LocalDate = currentEndDate): Boolean

    protected abstract fun isBeginningDateValid(date: LocalDate): Boolean

    abstract fun setNoEndDateDurationState()

    abstract fun setDaysDurationState(days: Int = currentDaysDuration)

    abstract fun setEndDateDurationState(endDate: LocalDate = currentEndDate)

    fun getDuration(): Duration = durationState.convertToDuration()

    fun getExpirationDate() = durationState.calculateEndDate(beginningDate)

    companion object


}