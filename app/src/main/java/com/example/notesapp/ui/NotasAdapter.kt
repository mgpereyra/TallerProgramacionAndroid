package com.example.notesapp.ui

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.model.Nota
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notas_items.view.*

class NotasAdapter : RecyclerView.Adapter<ViewHolder>() {

    var listaNotas: MutableList<Nota> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.notas_items,parent,false))
    }

    override fun getItemCount(): Int {
        return this.listaNotas.size
    }

    fun submitList(it: List<Nota>) {
        listaNotas.clear()
        listaNotas.addAll(it)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.setText(listaNotas.get(position).description)
        holder.imageView.loadUrl(listaNotas.get(position).srcImagen)
    }

    private fun ImageView.loadUrl(url: String) {
        Picasso.get().load(url).into(imageView)
    }
}