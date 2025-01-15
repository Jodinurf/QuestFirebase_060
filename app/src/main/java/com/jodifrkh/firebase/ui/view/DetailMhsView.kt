package com.jodifrkh.firebase.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jodifrkh.firebase.ui.viewmodel.DetailMhsViewModel
import com.jodifrkh.firebase.ui.viewmodel.MahasiswaEvent
import com.jodifrkh.firebase.ui.viewmodel.PenyediaViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMhsView(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailMhsViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.detailMhsUiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Mahasiswa") },
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.value.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.value.isError -> {
                    Text(
                        text = "Terjadi kesalahan saat memuat data.",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                }
                uiState.value.isUiMahasiswaEventNotEmpty -> {
                    ItemDetailMhs(
                        mahasiswa = uiState.value.detailMhsUiEvent
                    )
                }
                else -> {
                    Text(
                        text = "Data mahasiswa tidak ditemukan.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Composable
fun ItemDetailMhs(
    modifier: Modifier = Modifier,
    mahasiswa: MahasiswaEvent
) {
    Card(
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ComponentDetailMhs(judul = "NIM", isinya = mahasiswa.nim)
            ComponentDetailMhs(judul = "Nama", isinya = mahasiswa.nama)
            ComponentDetailMhs(judul = "Alamat", isinya = mahasiswa.alamat)
            ComponentDetailMhs(judul = "Jenis Kelamin", isinya = mahasiswa.jenisKelamin)
            ComponentDetailMhs(judul = "Kelas", isinya = mahasiswa.kelas)
            ComponentDetailMhs(judul = "Angkatan", isinya = mahasiswa.angkatan)
            ComponentDetailMhs(judul = "Judul Skripsi", isinya = mahasiswa.judulSkripsi)
            ComponentDetailMhs(judul = "Dosen Pembimbing 1", isinya = mahasiswa.dosenPembimbing1)
            ComponentDetailMhs(judul = "Dosen Pembimbing 2", isinya = mahasiswa.dosenPembimbing2)
        }
    }
}

@Composable
fun ComponentDetailMhs(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
){
    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start)
    {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}