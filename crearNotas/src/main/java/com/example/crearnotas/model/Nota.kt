package com.example.crearnotas.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Nota(
    val id:Long,
    val description:String,
    val srcImagen:String
)