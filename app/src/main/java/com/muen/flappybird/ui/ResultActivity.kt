package com.muen.flappybird.ui

import android.content.Intent
import com.muen.flappybird.MainActivity
import com.muen.flappybird.databinding.ActivityResultBinding
import com.muen.flappybird.util.BaseActivity

class ResultActivity : BaseActivity<ActivityResultBinding>() {
    override fun onCreateViewBinding(): ActivityResultBinding {
        return ActivityResultBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        getScore()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnRestart.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }

        viewBinding.btnReturn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }



    private fun getScore(){
        val sp = getSharedPreferences("sonuc", MODE_PRIVATE)
        val enYuksekSkor = sp.getInt("EnyuksekSkor",0)

        val gelenSkor = intent.getIntExtra("skor",0)
        viewBinding.score.text = gelenSkor.toString()

        if (gelenSkor > enYuksekSkor){
            val editor = sp.edit()
            editor.putInt("EnyuksekSkor",gelenSkor)
            editor.commit()
            viewBinding.bestScore.text = gelenSkor.toString()
        }
        else{
            viewBinding.bestScore.text = enYuksekSkor.toString()
        }

    }
}