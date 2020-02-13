package com.example.taskapp.fragments.addReminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskapp.MainActivity
import com.example.taskapp.databinding.AddReminderFragmentBinding
import com.example.taskapp.di.viewModel
import com.example.taskapp.viewmodels.addReminder.AddReminderViewModel
import com.google.android.material.radiobutton.MaterialRadioButton
import org.threeten.bp.format.DateTimeFormatter


class AddReminderFragment : Fragment() {

    companion object {
        fun newInstance() = AddReminderFragment()
        const val END_DATE_TAG: String = "END DATE DIALOG"
        const val BEGINNING_DATE_TAG = "Beginning date Dialog"
        val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    }

    private val viewModel: AddReminderViewModel by viewModel {
        (activity as MainActivity).appComponent.addReminderViewModelFactory
            .create(args.taskDetails)
    }
    private lateinit var binding: AddReminderFragmentBinding
    private val args: AddReminderFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddReminderFragmentBinding
            .inflate(inflater, container, false)

        viewModel.getToastText().observe(viewLifecycleOwner, Observer { id ->
            if (id != null) {
                Toast.makeText(context, getString(id), Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupBinding()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewModel = null

    }

    private fun setupBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@AddReminderFragment.viewModel
            setTimeOfNotification.setOnClickListener {
                NotificationTimePickerFragment(this@AddReminderFragment.viewModel).show(
                    childFragmentManager,
                    "tag"
                )
            }
            confirmButton.setOnClickListener { addTaskWithReminder() }
        }
        setupDurationLayout()
        setupFrequencyLayout()
    }


    private fun setupDurationLayout() {
        binding.apply {
            beginningDateBtn.setOnClickListener { showBegDatePickerDialog() }
            setDurationDaysBtn.setOnClickListener { showDurationDaysPickerDialog() }
            setEndDateBtn.setOnClickListener { showEndDatePickerDialog() }
            durationRadioGroup.apply {
                setDurationButtonsVisibility(checkedRadioButtonId) //to show proper one on rotation
                setOnCheckedChangeListener { _, id ->
                    onDurationRadioChecked(id)
                }
            }
        }
    }

    private fun onDurationRadioChecked(id: Int) {
        val durationModel = viewModel.durationModel
        when (activity?.findViewById<View>(id)!!) {
            binding.xDaysDurationRadio -> {
                durationModel.setDaysDurationState()
            }
            binding.endDateRadio -> {
                durationModel.setEndDateDurationState()
            }
            binding.noEndDateRadio -> {
                durationModel.setNoEndDateDurationState()
            }
            else -> throw NoSuchElementException("There is no matching button")
        }
        setDurationButtonsVisibility(id)
    }


    private fun setupFrequencyLayout() {
        binding.apply {
            frequencyRadioGroup.apply {
                setFrequencyButtonsVisibility(checkedRadioButtonId) //on rotation
                setOnCheckedChangeListener { _, id ->
                    onFrequencyRadioCheck(id)
                    setFrequencyButtonsVisibility(id)
                }
            }
            setDailyFrequencyBtn.setOnClickListener { showFrequencyPickerDialog() }
            setDaysOfWeekBtn.setOnClickListener { showDaysOfWeekPickerDialog() }
        }
    }


    private fun onFrequencyRadioCheck(id: Int) {
        val frequencyModel = viewModel.frequencyModel
        when (activity?.findViewById<MaterialRadioButton>(id)) {
            binding.dailyFreqRadio -> {
                frequencyModel.setDailyFrequency()
            }
            binding.daysOfWeekRadio -> {
                frequencyModel.setDaysOfWeekFrequency()
            }

            else -> throw NoSuchElementException("There is no matching button")
        }

        setFrequencyButtonsVisibility(id)

    }


    private fun addTaskWithReminder() {
        viewModel.saveTaskWithReminder()
        findNavController().navigate(
            AddReminderFragmentDirections.navigationAddReminderToNavigationMyTasks()
        )
    }


    private fun showDurationDaysPickerDialog() {
        DaysDurationPickerFragment(viewModel).show(childFragmentManager, "days duration dialog")
    }


    private fun showDaysOfWeekPickerDialog() {
        WeekDayPickerFragment(viewModel).show(childFragmentManager, "weekday picker dialog")
    }


    private fun showFrequencyPickerDialog() {
        FrequencyPickerFragment(viewModel).show(childFragmentManager, "FREQUENCY PICKER DIALOG")
    }


    private fun showEndDatePickerDialog() {
        EndDatePickerFragment(viewModel).show(childFragmentManager, END_DATE_TAG)
    }

    private fun showBegDatePickerDialog() {
        BeginningDatePickerFragment(viewModel).show(childFragmentManager, BEGINNING_DATE_TAG)
    }


    private fun changeViewsHelper(currentViews: List<View>?, allViews: List<View>) {
        val currentList = currentViews ?: emptyList()
        changeViewsVisibility(currentList, allViews - currentList)
    }

    /**
     * function responsible for changing visibility of buttons under RadioGroup depending
     * on current radio  checked
     */
    private fun setDurationButtonsVisibility(id: Int) {
        val allBtns = listOf(
            binding.setDurationDaysBtn,
            binding.setEndDateBtn
        )
        when (activity?.findViewById<View>(id)!!) {
            binding.xDaysDurationRadio -> changeViewsHelper(
                listOf(binding.setDurationDaysBtn),
                allBtns
            )
            binding.endDateRadio -> changeViewsHelper(listOf(binding.setEndDateBtn), allBtns)
            binding.noEndDateRadio -> changeViewsHelper(null, allBtns)
            else -> throw NoSuchElementException("There is no matching button")
        }
    }

    /**
     * function responsible for changing visibility of buttons under RadioGroup depending
     * on current radio  checked
     */
    private fun setFrequencyButtonsVisibility(id: Int) {
        val allBtns = listOf(
            binding.setDailyFrequencyBtn,
            binding.setDaysOfWeekBtn
        )
        when (activity?.findViewById<MaterialRadioButton>(id)) {
            binding.dailyFreqRadio -> changeViewsHelper(
                listOf(binding.setDailyFrequencyBtn),
                allBtns
            )
            binding.daysOfWeekRadio -> changeViewsHelper(listOf(binding.setDaysOfWeekBtn), allBtns)
            else -> throw NoSuchElementException("There is no matching button")
        }

    }


}


fun <T : View> Fragment.changeViewsVisibility(
    visibleViews: List<T>,
    goneViews: List<T>
) {
    visibleViews.forEach { view ->
        if (!view.isVisible) view.visibility = View.VISIBLE
    }
    goneViews.forEach { view ->
        if (view.isVisible) view.visibility = View.GONE
    }
}
