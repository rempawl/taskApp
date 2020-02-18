package com.example.taskapp.viewmodels

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.R
import com.example.taskapp.database.entities.Reminder
import com.example.taskapp.database.entities.Task
import com.example.taskapp.repos.task.TaskRepository
import com.example.taskapp.viewmodels.addTask.TaskDetailsModel
import com.example.taskapp.viewmodels.reminder.DurationModel
import com.example.taskapp.viewmodels.reminder.FrequencyModel
import com.example.taskapp.viewmodels.reminder.NotificationModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class EditTaskViewModel @AssistedInject constructor(
    @Assisted val task: Task,
    private val taskRepo: TaskRepository,
    val taskDetailsModel: TaskDetailsModel,
    durationModelFactory: DurationModel.Factory,
    frequencyModelFactory: FrequencyModel.Factory,
    notificationModelFactory: NotificationModel.Factory
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(task: Task): EditTaskViewModel
    }

    val durationModel: DurationModel
    val frequencyModel: FrequencyModel
    val notificationModel: NotificationModel

    init {
        val reminder = task.reminder
        if (reminder != null) {
            durationModel = durationModelFactory.create(reminder.duration, reminder.begDate)
            frequencyModel = frequencyModelFactory.create(reminder.frequency)
            notificationModel = notificationModelFactory.create(reminder.notificationTime)
        } else {
            notificationModel = notificationModelFactory.create()
            durationModel = durationModelFactory.create()
            frequencyModel = frequencyModelFactory.create()

        }
        taskDetailsModel.taskDescription = task.description
    }

    val isReminderSwitchChecked = ObservableField<Boolean>(task.reminder != null)


    private val errorCallback by lazy(LazyThreadSafetyMode.NONE) {
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (sender != null) {
                    val value = (sender as ObservableField<Boolean>).get()
                    if (value == true) {
                        sender.set(false)
                        when (sender) {
                            durationModel.begDateError -> {
                                toastText.value = R.string.beginning_date_error

                            }
                            durationModel.endDateError -> {
                                toastText.value = R.string.end_date_error
                            }
                        }
                    } else {
                        toastText.value = null
                    }

                }

            }

        }

    }

    init {
        durationModel.endDateError.addOnPropertyChangedCallback(errorCallback)
        durationModel.begDateError.addOnPropertyChangedCallback(errorCallback)
    }


    private val toastText = MutableLiveData<Int>(null)
    fun getToastText(): LiveData<Int> = toastText


    override fun onCleared() {
        super.onCleared()
        durationModel.begDateError.removeOnPropertyChangedCallback(errorCallback)
        durationModel.endDateError.removeOnPropertyChangedCallback(errorCallback)

    }

    fun saveEditedTask() {
        viewModelScope.launch {
            var reminder: Reminder? = null
            if (isReminderSwitchChecked.get() == true) {
                reminder = Reminder(
                    begDate = durationModel.beginningDate,
                    frequency = frequencyModel.getFrequency(),
                    duration = durationModel.getDuration(),
                    notificationTime = notificationModel.getNotificationTime(),
                    expirationDate = durationModel.getExpirationDate(),
                    updateDate = frequencyModel.getUpdateDate(durationModel.beginningDate)
                )
            }
            val edited = task.copy(
                description = taskDetailsModel.taskDescription,
                reminder = reminder
            )
            taskRepo.updateTask(edited)
        }
    }
}




