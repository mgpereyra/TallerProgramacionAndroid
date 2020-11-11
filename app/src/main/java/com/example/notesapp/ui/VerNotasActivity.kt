package com.example.notesapp.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.databinding.ActivityVerNotasBinding
import kotlinx.android.synthetic.main.activity_ver_notas.*
import org.koin.android.viewmodel.ext.android.viewModel

class VerNotasActivity : AppCompatActivity() {
    private lateinit var adapter: NotasAdapter
    private lateinit var binding: ActivityVerNotasBinding
    private val viewModel: NotasViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = NotasAdapter()
        with(binding.recyclerView) {
            layoutManager =
                LinearLayoutManager(this@VerNotasActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = this@VerNotasActivity.adapter
        }
        viewModel.notasLiveData.observe(this, Observer {
            adapter.submitList(it.reversed())
            adapter.notifyDataSetChanged()
        })

        navegarHaciaAtras()
        comprobarPermisos()
    }

    private fun comprobarPermisos() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){
                val permisoAlmacenamiento = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permisoAlmacenamiento, 123)
            }
        }else{
            Toast.makeText(this,
                "Función no disponible en esta versión de Android...",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun navegarHaciaAtras() {
        btnVolver.setOnClickListener { onBackPressed() }
    }
}