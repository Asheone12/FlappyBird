package com.muen.flappybird.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.muen.flappybird.MainActivity
import com.muen.flappybird.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var ulas : ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        ulas = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(ulas.root)

        skorYaz()

        ulas.buttonYenidenBasla.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }

        ulas.buttonAnaMenuDon.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    fun skorYaz(){
        val sp = getSharedPreferences("sonuc", MODE_PRIVATE)
        val enYuksekSkor = sp.getInt("EnyuksekSkor",0)

        val gelenSkor = intent.getIntExtra("skor",0)
        ulas.textViewSonucSkor.text = gelenSkor.toString()

        if (gelenSkor > enYuksekSkor){
            val editor = sp.edit()
            editor.putInt("EnyuksekSkor",gelenSkor)
            editor.commit()
            ulas.textViewSonucEnYuksekSkor.text = gelenSkor.toString()
        }
        else{
            ulas.textViewSonucEnYuksekSkor.text = enYuksekSkor.toString()
        }

    }
}