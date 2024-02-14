package com.example.androidmvvionlinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.androidmvvionlinedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val apiDataViewModel: ApiDataViewModel by viewModels()
    private lateinit var vb: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                apiDataViewModel.uiState.collectLatest {
                    vb.progressBar.isVisible = it.isLoading
                    val text = if (it.isSuccess) {
                        it.model?.message
                    } else{
                        "Error"
                    }
                    vb.textView.text = text
                }
            }
        }
    }

}