package com.example.taskapp.utils

import com.example.taskapp.MyApp.Companion.TODAY
import com.example.taskapp.data.reminder.NotificationTime
import com.example.taskapp.data.reminder.Reminder
import com.example.taskapp.database.entities.task.DbTask
import com.example.taskapp.viewmodels.reminder.ReminderDurationState
import com.example.taskapp.viewmodels.reminder.ReminderFrequencyState
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

object DbTasks {
    private val frequencyMondayTuesday =
        ReminderFrequencyState.WeekDays(setOf(DayOfWeek.TUESDAY.value, DayOfWeek.MONDAY.value))
    private val frequency2days = ReminderFrequencyState.Daily(2)

    private val duration10Days = ReminderDurationState.DaysDuration(10)
    private val durationEndDate = ReminderDurationState.EndDate(
        LocalDate.ofEpochDay(TODAY.toEpochDay() + 20)
    )

    private val notificationTimeTwelve =
        NotificationTime(
            12,
            0,
            true
        )

    private val firstReminder =
        Reminder(
            begDate = TODAY,
            frequency = frequencyMondayTuesday.convertToFrequency(),
            duration = duration10Days.convertToDuration(),
            expirationDate = duration10Days.calculateEndDate(begDate = TODAY),
            realizationDate = frequencyMondayTuesday.calculateRealizationDate(TODAY, true),
            notificationTime = notificationTimeTwelve
        )

    private val secondReminder =
        Reminder(
            begDate = TODAY,
            frequency = frequency2days.convertToFrequency(),
            duration = durationEndDate.convertToDuration(),
            expirationDate = durationEndDate.calculateEndDate(TODAY),
            realizationDate = frequency2days.calculateRealizationDate(TODAY, true),
            notificationTime = notificationTimeTwelve
        )

    val tasks = mutableListOf<DbTask>(
        DbTask(
            taskID = 0,
            name = "first",
            description = "no reminder"
        ),
        DbTask(
            taskID = 1,
            name = "second",
            description = "with first reminder",
            reminder = firstReminder
        ),
        DbTask(
            taskID = 2,
            name = "third",
            description = "with second reminder",
            reminder = secondReminder
        ),
        DbTask(
            taskID = 3,
            name = "fourth",
            description = "with first reminder",
            reminder = firstReminder
        ),
        DbTask(
            taskID = 4,
            name = "fifth",
            description = "with second reminder",
            reminder = secondReminder
        )
    )

    val minimalTasks = tasks.map { task -> task.toTaskMinimal() }

    val errorTask = DbTask(
        taskID = -1,
        name = "error"
    )

}