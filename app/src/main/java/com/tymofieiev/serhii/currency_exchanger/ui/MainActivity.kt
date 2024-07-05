package com.tymofieiev.serhii.currency_exchanger.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tymofieiev.serhii.currency_exchanger.databinding.MainActivityBinding
import com.tymofieiev.serhii.currency_exchanger.extention.viewBinding


/*
* Created by Serhii Tymofieiev
*/
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(MainActivityBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}