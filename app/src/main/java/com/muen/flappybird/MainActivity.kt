package com.muen.flappybird

import android.content.Intent
import android.media.MediaPlayer
import com.muen.flappybird.databinding.ActivityMainBinding
import com.muen.flappybird.ui.GameActivity
import com.muen.flappybird.util.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private var muzukcalar = MediaPlayer()

    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        getBestScore()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.buttonBasla.setOnClickListener {
            startActivity(Intent(this@MainActivity, GameActivity::class.java))
        }
    }


    private fun getBestScore(){
        val sp = getSharedPreferences("sonuc", MODE_PRIVATE)
        val enYuksekSkor = sp.getInt("EnyuksekSkor",0)
        viewBinding.score.text = enYuksekSkor.toString()
    }

    override fun onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid())
        super.onBackPressed()
    }
}