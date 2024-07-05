package com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tymofieiev.serhii.currency_exchanger.R
import com.tymofieiev.serhii.currency_exchanger.databinding.ExchangeFragmentBinding
import com.tymofieiev.serhii.currency_exchanger.extention.InputFilterFractionDigestLength
import com.tymofieiev.serhii.currency_exchanger.extention.addGradientItemDecoration
import com.tymofieiev.serhii.currency_exchanger.extention.launchAndRepeatWithViewLifecycle
import com.tymofieiev.serhii.currency_exchanger.extention.parcelable
import com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger.dialog.OperationSuccessDialogData
import com.tymofieiev.serhii.currency_exchanger.ui.currency_picker.CurrencyPickerBottomSheet
import com.tymofieiev.serhii.currency_exchanger.ui.currency_picker.CurrencyPickerParameters
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


/*
* Created by Serhii Tymofieiev
*/
class ExchangeFragment : Fragment() {
    private lateinit var binding: ExchangeFragmentBinding
    private val viewModel: ExchangeViewModel by viewModel()
    private val balanceAdapter = BalanceAdapter()
    private val historyAdapter = ExchangeOperationsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CurrencyPickerBottomSheet.REQUEST_KEY) { _, bundle ->
            val selectedCurrency =
                bundle.parcelable<CurrencyPickerParameters>(CurrencyPickerBottomSheet.SELECTED)
            requireNotNull(selectedCurrency)
            viewModel.onCurrencySelected(selectedCurrency)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ExchangeFragmentBinding.inflate(inflater, container, false)
            .apply {
                binding = this
                vm = viewModel
                lifecycleOwner = viewLifecycleOwner
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observersViewModel()
        binding.rvBalances.adapter = balanceAdapter
        binding.rvOperation.apply {
            adapter = historyAdapter
            addGradientItemDecoration()
        }
        binding.etSellValue.filters = arrayOf(
            InputFilterFractionDigestLength(2)
        )
    }

    private fun observersViewModel() {
        launchAndRepeatWithViewLifecycle {
            viewModel.balanceList.onEach {
                balanceAdapter.submitList(it)
            }.launchIn(this)
            viewModel.currencyPickerData.onEach {
                findNavController().navigate(
                    ExchangeFragmentDirections.toCurrencyPicker(it)
                )
            }.launchIn(this)
            viewModel.successDialogData.onEach {
                showSuccessDialog(it)
            }.launchIn(this)
            viewModel.excOperationList.onEach {
                historyAdapter.submitList(it)
            }.launchIn(this)
        }
    }

    private fun showSuccessDialog(it: OperationSuccessDialogData) {
        val message = if (it.part2.isNotEmpty()) String.format(
            getString(R.string.dialog_success_convert_message_with_commission),
            it.part0,
            it.part1,
            it.part2
        ) else String.format(getString(R.string.dialog_success_convert_message), it.part0, it.part1)
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_success_convert_title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.button_done)) { _, _ ->
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}