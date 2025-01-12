package com.jodifrkh.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jodifrkh.firebase.model.Mahasiswa
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkMahasiswaRepository (
    private val firestore: FirebaseFirestore
) : MahasiswaRepository {

    override suspend fun getMahasiswa(): Flow<List<Mahasiswa>> = callbackFlow{
        val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nim", Query.Direction.ASCENDING)
            .addSnapshotListener{ value, error ->
                if (value != null){
                    val mhsList = value.documents.mapNotNull {
                        it.toObject(Mahasiswa::class.java)!!
                    }
                    trySend(mhsList) // try send memberikan fungsi untuk mengirim data ke flow
                }
            }
        awaitClose {
            mhsCollection.remove()
        }
    }

    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        try {
            // jika ingin menyimpan dokumen ID berdasarkan NIM, gunakan komentar
//            val nim = mahasiswa.nim
//            if (nim.isNotEmpty()) {
//                throw Exception("NIM Tidak boleh kosong")
//            }
            firestore.collection("Mahasiswa")
                //.document(nim)
                //set(mahasiswa) jangan gunakan add(mahasiswa) karena akan otomatis Auto ID Documents
                .add(mahasiswa)
                .await()
        } catch (e: Exception) {
            throw Exception("Gagal menambahkan data Mahasiswa: ${e.message}")
        }
    }

    override suspend fun updateMahasiswa(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .set(mahasiswa)
                .await()
        } catch (e: Exception) {
            throw Exception("Gagal Mengupdate data Mahasiswa: ${e.message}")
        }
    }

    // gunakan fungsi dibawah untuk proses penghapusan data berdasarkan field, karena dokumen ID auto ID, bukan NIM
    override suspend fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        try {
            val querySnapshot = // membuat variabel terlebih dahulu
                firestore.collection("Mahasiswa")
                    .whereEqualTo("nim", mahasiswa.nim) // menggunakan fungsi whereEqualto untuk membuat query yang mencari fields nim dalam koleksi Mahasiswa
                    .get() // mengambil data
                    .await()

            for (document in querySnapshot.documents) { // melakukan perulangan dokumen selama variabel mencari nim
                document.reference // menunjuk dokumen yang terkait berdasarkan perulangan
                    .delete() // melakukan penghapusan dokumen
                    .await()
            }
        } catch (e: Exception) {
            throw Exception("Gagal Menghapus data Mahasiswa: ${e.message}")
        }
    }

    // Gunakan fungsi di bawah apabila dokumen ID adalah nim yang akan dihapus, bukan auto ID
//    override suspend fun deleteMahasiswa(mahasiswa: Mahasiswa) {
//        try {
//            firestore.collection("Mahasiswa")
//                .document(mahasiswa.nim)
//                .delete()
//                .await()
//        } catch (e: Exception) {
//            throw Exception("Gagal Menghapus data Mahasiswa: ${e.message}")
//        }
//    }

    override suspend fun getMahasiswaByNim(nim: String): Flow<Mahasiswa> = callbackFlow {
        val mhsDocument = firestore.collection("Mahasiswa")
            .document(nim)
            .addSnapshotListener {value, error ->
                if (value != null) {
                    val mhs = value.toObject(Mahasiswa::class.java)!!
                    trySend(mhs)
                }
            }

        awaitClose {
            mhsDocument.remove()
        }
    }

}