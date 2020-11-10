package com.example.notesapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="notas")
data class NotaEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="idNota")
    val id:Long=0,

    @ColumnInfo(name="description")
    val description:String,

    @ColumnInfo(name="srcImagen")
    val srcImagen:String
)