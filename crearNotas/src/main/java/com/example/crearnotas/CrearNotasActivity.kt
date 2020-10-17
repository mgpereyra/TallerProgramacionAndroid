package com.example.crearnotas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_crear_notas.*
import java.util.jar.Manifest

class CrearNotasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_notas)
        imageView.loadUrl(R.mipmap.ic_launcher_monalisa_round)
        buscarGaleria()
        tomarFotos()
        navegarHaciaAtras()
    }

    fun ImageView.loadUrl(url: Int) {
        Picasso.get().load(url).into(imageView)
    }

    fun buscarGaleria() {
        buttonGaleria.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p: View) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 100)
            }
        })
    }

    fun tomarFotos() {
        buttonSacarFotos.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(intent.resolveActivity(this.packageManager)!=null) {
                startActivityForResult(intent, 200)
            }
            else{
                Toast.makeText(this,"No se ha podido abrir la camara",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navegarHaciaAtras() {
        btnCancelar.setOnClickListener { onBackPressed() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageView.setImageURI(data!!.data)
        }
        if (requestCode == 200 && resultCode == RESULT_OK) {
            val takeImage = data?.extras?.get("Data") as Bitmap
            imageView.setImageBitmap(takeImage)
        }
    }
}