package com.me.tiptime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.me.tiptime.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    // Update UI elements
                    if (uiState.costOfService != null) {
                        var tip = uiState.serviceState * uiState.costOfService

                        if (uiState.isRoundTip) {
                            tip = kotlin.math.ceil(tip)
                        }

                        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
                        binding.tipAmount.text = getString(R.string.tip_amount, formattedTip)
                    }
                }
            }
        }
        binding.switchRound.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeSwitch(isChecked)
        }
        binding.costOfServiceEditText.addTextChangedListener { edt ->
            viewModel.changeCost(edt.toString())
        }
        binding.tipOptions.setOnCheckedChangeListener{ group, checkedId ->
            val tipPercentage = when (checkedId) {
                R.id.option_20 -> 0.20
                R.id.option_18 -> 0.18
                else -> 0.15
            }
            viewModel.changeTipOptions(tipPercentage)
        }

//        binding.buttonCalculate.setOnClickListener { calculateTip() }

    }

    private fun calculateTip() {
        val stringIntTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringIntTextField.toDoubleOrNull()
        if (cost == null) {
            binding.tipAmount.text = ""
            return
        }
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_20 -> 0.20
            R.id.option_18 -> 0.18
            else -> 0.15
        }
        var tip = tipPercentage * cost

        val roundUp = binding.switchRound.isChecked
        if (roundUp) {
            tip = kotlin.math.ceil(tip)
        }

        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipAmount.text = getString(R.string.tip_amount, formattedTip)
    }
}