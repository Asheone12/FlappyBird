package com.muen.flappybird

import android.content.Intent
import com.muen.flappybird.databinding.ActivityMainBinding
import com.muen.flappybird.ui.GameActivity
import com.muen.flappybird.util.BaseActivity
import com.muen.flappybird.util.MMKVManager

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        super.initView()
        getBestScore()
    }

    override fun initListener() {
        super.initListener()
        viewBinding.btnStart.setOnClickListener {
            startActivity(Intent(this@MainActivity, GameActivity::class.java))
        }
    }


    private fun getBestScore(){
        viewBinding.bestScore.text = MMKVManager.bestScore.toString()
    }

    override fun onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid())
        super.onBackPressed()
    }
}