package com.example.notas.ui

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notas.model.Nota
import com.example.notas.model.NotasRepository

class NotasViewModel(
    private val notasRepository: NotasRepository
) : ViewModel() {

    val notasLiveData = MutableLiveData<List<Nota>>()

    init {
        notasLiveData.value=notasRepository.getAll()
    }

    fun getNotasList() {
        notasLiveData.value=notasRepository.getAll()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "on cleared called")
    }
}