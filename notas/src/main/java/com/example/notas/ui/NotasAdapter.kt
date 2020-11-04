package com.example.notas.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.R
import com.example.notas.data.NotaEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notas_items.view.*

class NotasAdapter : RecyclerView.Adapter<ViewHolder>() {

    var listaNotas: MutableList<NotaEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(
                R.layout.notas_items,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return this.listaNotas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.setText(listaNotas.get(position).description)
        holder.imageView.loadUrl(listaNotas.get(position).srcImagen)
    }

    fun ImageView.loadUrl(url: String) {
        Picasso.get().load(url).into(imageView)
    }

    fun setearLista(lista:MutableList<NotaEntity>){
        listaNotas = lista
        notifyDataSetChanged()
    }
}