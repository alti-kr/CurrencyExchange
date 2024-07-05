package com.tymofieiev.serhii.currency_exchanger.ui.currency_picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tymofieiev.serhii.currency_exchanger.R
import com.tymofieiev.serhii.currency_exchanger.databinding.CurrencyPickerBottomSheetBinding
import com.tymofieiev.serhii.currency_exchanger.extention.SimpleTextWatcher
import com.tymofieiev.serhii.currency_exchanger.extention.addGradientItemDecoration
import com.tymofieiev.serhii.currency_exchanger.extention.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/*
* Created by Serhii Tymofieiev
*/
class CurrencyPickerBottomSheet : BottomSheetDialogFragment() {
    private val navArgs by navArgs<CurrencyPickerBottomSheetArgs>()
    private val viewModel: CurrencyPickerViewModel by viewModel { parametersOf(id) }
    private lateinit var binding: CurrencyPickerBottomSheetBinding
    private val adapterSymbols = CurrencyPickerAdapter {
        viewModel.onSelected(it)
    }
    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(supervisorJob + Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return CurrencyPickerBottomSheetBinding.inflate(inflater, container, false)
            .apply {
                binding = this
                lifecycleOwner = viewLifecycleOwner
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithViewLifecycle {
            viewModel.data.onEach {
                adapterSymbols.setup(it)
            }.launchIn(this)

            viewModel.result.onEach {
                setFragmentResult(REQUEST_KEY, bundleOf(SELECTED to it))
                findNavController().popBackStack()
            }.launchIn(this)
        }
        binding.rv.apply {
            adapter = this@CurrencyPickerBottomSheet.adapterSymbols
            addGradientItemDecoration()
        }
        binding.etSearch.addTextChangedListener(SimpleTextWatcher {
            searchCountries(it)
        })
        binding.imageButtonClose.setOnClickListener {
            dismiss()
        }
    }
    private fun searchCountries(query: String?) {
        scope.launch {
            query?.let {
                val symbols = viewModel.data.value
                val filtered = symbols.filter {
                    it.startsWith(query) ||
                            it.lowercase()
                                .contains(query.lowercase())
                }
                binding.rv.post {
                    adapterSymbols.setup(filtered)
                }
            }
        }
    }
    companion object {
        const val REQUEST_KEY = "RK_PRICE_OR_DISTANCE"
        const val SELECTED = "SELECTED_PRICE_OR_DISTANCE"
    }
}