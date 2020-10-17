package com.example.notas.viewmodel

import android.content.ContentValues.TAG
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notas.R
import com.example.notas.data.Notas

class NotasViewModel : ViewModel() {

    lateinit var listNotas: MutableLiveData<MutableList<Notas>>

    fun getNotasList(): LiveData<MutableList<Notas>> {
        this.listNotas = MutableLiveData()
        loadNotas()
        return listNotas
    }

    private fun loadNotas() {
        val myHandler = Handler()
        myHandler.postDelayed({
            val notasList: MutableList<Notas> = ArrayList()
            notasList.add(
                Notas("https://images4.alphacoders.com/548/thumb-1920-54846.png",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit mollis magnis dapibus nascetur, purus."
                )
            )
            notasList.add(
                Notas(
                    "https://static.highsnobiety.com/thumbor/Be530Ob8QTa6U2zPM90eZYuU4Fs=/1600x1067/static.highsnobiety.com/wp-content/uploads/2019/10/08114400/breaking-bad-main.jpg",
                    "Accumsan ad lacinia odio lacus facilisi mus ullamcorper augue donec cras, tempor penatibus."
                )
            )
            notasList.add(
                Notas(
                   "https://m.media-amazon.com/images/M/MV5BMzgxMmQxZjQtNDdmMC00MjRlLTk1MDEtZDcwNTdmOTg0YzA2XkEyXkFqcGdeQXVyMzQ2MDI5NjU@._V1_.jpg",
                    "Odio interdum netus mauris felis in, scelerisque eget mollis praesent, hendrerit aptent pulvinar nibh."
                )
            )
            notasList.add(
                Notas(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRhdE9nFHD84_zeesDSobz3EaKMBEJ9_XynpQ&usqp=CAU",
                    "Nostra integer nunc tempus semper gravida nullam, ad convallis eleifend enim montes vehicula."
                )
            )

            notasList.add(
                Notas("https://static.highsnobiety.com/thumbor/Be530Ob8QTa6U2zPM90eZYuU4Fs=/1600x1067/static.highsnobiety.com/wp-content/uploads/2019/10/08114400/breaking-bad-main.jpg",
                    "Mi laoreet class rhoncus fringilla sapien morbi, turpis facilisis justo eget praesent sagittis inceptos, vel."
                )
            )
            notasList.add(
                Notas(
                    "https://images4.alphacoders.com/548/thumb-1920-54846.png",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit mollis magnis dapibus nascetur, purus."
                )
            )
            notasList.add(
                Notas(
                    "https://m.media-amazon.com/images/M/MV5BMzgxMmQxZjQtNDdmMC00MjRlLTk1MDEtZDcwNTdmOTg0YzA2XkEyXkFqcGdeQXVyMzQ2MDI5NjU@._V1_.jpg",
                    "Magnis sociosqu inceptos bibendum tristique in at orci quam blandit vel lobortis montes."
                )
            )
            notasList.add(
                Notas(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRhdE9nFHD84_zeesDSobz3EaKMBEJ9_XynpQ&usqp=CAU",
                    "Ultrices justo lacinia placerat bibendum lobortis id odio at felis facilisis, praesent tempor cras."
                )
            )
            this.listNotas.value = notasList
        }, 0)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "on cleared called")
    }
}