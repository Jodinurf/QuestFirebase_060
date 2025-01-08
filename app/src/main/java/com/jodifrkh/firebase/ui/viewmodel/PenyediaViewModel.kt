package com.jodifrkh.firebase.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jodifrkh.firebase.MahasiswaApplication

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(MahasiswaApplication().container.mahasiswaRepository)
        }
    }
}

fun CreationExtras.MahasiswaApplication() : MahasiswaApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MahasiswaApplication)