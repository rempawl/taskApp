package com.example.taskapp.viewModels.addReminder

import com.example.taskapp.viewmodels.addReminder.DurationModel
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

//todo
 class DurationModelTest{
    private lateinit var model : DurationModel
     @Before
    fun setup(){
         model = DurationModel()
     }

     @Test
     fun beginningDate_yesterday_error(){
     }

     @Test
     fun beginningDate_afterEndDate_error(){

     }

     @Test
     fun endDate_beforeBegDate_error(){

     }

     @Test
     fun endDate_yesterday_error(){


     }

    @Test
    fun durationState_endDateBeforeBegDate_confirmBtnDisabled(){

    }


}