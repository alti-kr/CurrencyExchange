package com.tymofieiev.serhii.currency_exchanger.ui.currency_picker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/*
* Created by Serhii Tymofieiev
*/
class CurrencyPickerViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val parameters: StateFlow<CurrencyPickerParameters?> =
        savedStateHandle.getStateFlow("parameters", null).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )
    val data = parameters.filterNotNull().map {
        it.items
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )


    private val _result = MutableSharedFlow<CurrencyPickerParameters>()
    val result = _result.asSharedFlow()


    fun onSelected(item: String) {
        viewModelScope.launch {
            if (parameters.value != null)
                _result.emit(CurrencyPickerParameters(parameters.value!!.isBuy, emptyList(), item))
        }
    }
}