package com.example.notas.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val imageView = view.findViewById<ImageView>(R.id.imageView)
    val description = view.findViewById<TextView>(R.id.description)
}