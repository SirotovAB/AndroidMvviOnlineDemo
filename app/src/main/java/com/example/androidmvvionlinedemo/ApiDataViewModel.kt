package com.example.androidmvvionlinedemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmvvionlinedemo.model.ApiModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AppDataUiState(
    val model: ApiModel? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = true,
)

class ApiDataViewModel: ViewModel() {
    private val _uiState = MutableStateFlow<AppDataUiState>(AppDataUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateData()
    }

    override fun onCleared() {
        httpClient.close()
    }
    fun updateData(){
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val images = getData()
                _uiState.update { it.copy(model = images) }
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: IOException){
                _uiState.update { it.copy(isLoading = false, isSuccess = false) }
            }

        }
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun getData(): ApiModel {
        return httpClient.get("https://sirotovab.github.io/testPuplicAPI/hello_api.json")
            .body<ApiModel>()
    }
}