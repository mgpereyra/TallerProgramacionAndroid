package com.example.notas.viewmodel

import android.content.ContentValues.TAG
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notas.data.Nota

class NotasViewModel : ViewModel() {

    lateinit var listNota: MutableLiveData<MutableList<Nota>>

    fun getNotasList(): LiveData<MutableList<Nota>> {
        this.listNota = MutableLiveData()
        loadNotas()
        return listNota
    }

    private fun loadNotas() {
        val myHandler = Handler()
        myHandler.postDelayed({
            val notaList: MutableList<Nota> = ArrayList()
            notaList.add(
                Nota("https://images4.alphacoders.com/548/thumb-1920-54846.png",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit mollis magnis dapibus nascetur, purus."
                )
            )
            notaList.add(
                Nota(
                    "https://static.highsnobiety.com/thumbor/Be530Ob8QTa6U2zPM90eZYuU4Fs=/1600x1067/static.highsnobiety.com/wp-content/uploads/2019/10/08114400/breaking-bad-main.jpg",
                    "Accumsan ad lacinia odio lacus facilisi mus ullamcorper augue donec cras, tempor penatibus."
                )
            )
            notaList.add(
                Nota(
                   "https://m.media-amazon.com/images/M/MV5BMzgxMmQxZjQtNDdmMC00MjRlLTk1MDEtZDcwNTdmOTg0YzA2XkEyXkFqcGdeQXVyMzQ2MDI5NjU@._V1_.jpg",
                    "Odio interdum netus mauris felis in, scelerisque eget mollis praesent, hendrerit aptent pulvinar nibh."
                )
            )
            notaList.add(
                Nota(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRhdE9nFHD84_zeesDSobz3EaKMBEJ9_XynpQ&usqp=CAU",
                    "Nostra integer nunc tempus semper gravida nullam, ad convallis eleifend enim montes vehicula."
                )
            )

            notaList.add(
                Nota("https://static.highsnobiety.com/thumbor/Be530Ob8QTa6U2zPM90eZYuU4Fs=/1600x1067/static.highsnobiety.com/wp-content/uploads/2019/10/08114400/breaking-bad-main.jpg",
                    "Mi laoreet class rhoncus fringilla sapien morbi, turpis facilisis justo eget praesent sagittis inceptos, vel."
                )
            )
            notaList.add(
                Nota(
                    "https://images4.alphacoders.com/548/thumb-1920-54846.png",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit mollis magnis dapibus nascetur, purus."
                )
            )
            notaList.add(
                Nota(
                    "https://m.media-amazon.com/images/M/MV5BMzgxMmQxZjQtNDdmMC00MjRlLTk1MDEtZDcwNTdmOTg0YzA2XkEyXkFqcGdeQXVyMzQ2MDI5NjU@._V1_.jpg",
                    "Magnis sociosqu inceptos bibendum tristique in at orci quam blandit vel lobortis montes."
                )
            )
            notaList.add(
                Nota(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRhdE9nFHD84_zeesDSobz3EaKMBEJ9_XynpQ&usqp=CAU",
                    "Ultrices justo lacinia placerat bibendum lobortis id odio at felis facilisis, praesent tempor cras."
                )
            )
            this.listNota.value = notaList
        }, 0)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "on cleared called")
    }
}