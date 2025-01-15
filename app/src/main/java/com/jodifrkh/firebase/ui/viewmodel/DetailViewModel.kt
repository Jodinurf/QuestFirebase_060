package com.jodifrkh.firebase.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jodifrkh.firebase.model.Mahasiswa
import com.jodifrkh.firebase.navigation.DestinasiDetail
import com.jodifrkh.firebase.repository.MahasiswaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn


class DetailMhsViewModel(
    savedStateHandle: SavedStateHandle,
    private val mhsRepository: MahasiswaRepository
) : ViewModel() {
    private val _nim : String = checkNotNull(savedStateHandle[DestinasiDetail.nim])

    val detailMhsUiState: StateFlow<DetailMhsUiState> = mhsRepository.getMahasiswaByNim(_nim)
        .filterNotNull()
        .map {
            DetailMhsUiState(
                detailMhsUiEvent = it.toDetailMhsUiEvent(),
                isLoading = false
            )
        }
        .onStart {
            emit(DetailMhsUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(DetailMhsUiState(
                isLoading = false,
                isError = true,
            ))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailMhsUiState(
                isLoading = true
            )
        )

}
data class DetailMhsUiState(
    val detailMhsUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMhsMessage: String = ""
) {
    val isUiMhsEmpty: Boolean
        get() = detailMhsUiEvent == MahasiswaEvent()

    val isUiMahasiswaEventNotEmpty: Boolean
        get() = detailMhsUiEvent != MahasiswaEvent()
}

fun Mahasiswa.toDetailMhsUiEvent() : MahasiswaEvent {
    return MahasiswaEvent(
        nim = nim,
        nama = nama,
        jenisKelamin = jenisKelamin,
        angkatan = angkatan,
        alamat = alamat,
        kelas = kelas,
        judulSkripsi = judulSkripsi,
        dosenPembimbing1 = dosenPembimbing1,
        dosenPembimbing2 = dosenPembimbing2
    )
}