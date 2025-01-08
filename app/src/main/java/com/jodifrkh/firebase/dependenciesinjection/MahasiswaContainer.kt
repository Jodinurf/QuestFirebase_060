package com.jodifrkh.firebase.dependenciesinjection

import com.google.firebase.firestore.FirebaseFirestore
import com.jodifrkh.firebase.repository.MahasiswaRepository
import com.jodifrkh.firebase.repository.NetworkMahasiswaRepository

interface AppContainer{
    val mahasiswaRepository: MahasiswaRepository
}

class MahasiswaContainer : AppContainer{
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
    override val mahasiswaRepository : MahasiswaRepository by lazy {
        NetworkMahasiswaRepository(firestore)
    }
}