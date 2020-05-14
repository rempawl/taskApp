package com.example.taskapp.di

import com.example.taskapp.utils.providers.DefaultSchedulerProvider
import com.example.taskapp.utils.providers.SchedulerProvider
import com.example.taskapp.viewmodels.reminder.durationModel.AddTaskDurationModel
import com.example.taskapp.viewmodels.reminder.durationModel.DurationModel
import com.example.taskapp.viewmodels.reminder.frequencyModel.AddTaskFrequencyModel
import com.example.taskapp.viewmodels.reminder.frequencyModel.FrequencyModel
import com.example.taskapp.viewmodels.reminder.notificationModel.AddTaskNotificationModel
import com.example.taskapp.viewmodels.reminder.notificationModel.NotificationModel
import com.example.taskapp.viewmodels.taskDetails.DefaultTaskDetailsModel
import com.example.taskapp.viewmodels.taskDetails.TaskDetailsModel
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object ModelModule {


    @Reusable
    @Provides
    @JvmStatic
    fun provideSchedulersProvider(): SchedulerProvider =
        DefaultSchedulerProvider()

    @Provides
    @JvmStatic
    fun provideTaskDetailsModel(): TaskDetailsModel =
        DefaultTaskDetailsModel()

    @Provides
    @JvmStatic
    fun provideNotificationModel(): NotificationModel =
        AddTaskNotificationModel()

    @Provides
    @JvmStatic
    fun provideDurationModel(): DurationModel =
        AddTaskDurationModel()

    @Provides
    @JvmStatic
    fun provideFrequencyModel(): FrequencyModel =
        AddTaskFrequencyModel()
}