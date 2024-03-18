package com.muen.flappybird.ui

import android.content.Intent
import com.muen.flappybird.MainActivity
import com.muen.flappybird.databinding.ActivityResultBinding
import com.muen.flappybird.util.BaseActivity
import com.muen.flappybird.util.MMKVManager

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
        val bestScore = MMKVManager.bestScore
        val nowScore = intent.getIntExtra("score",0)
        viewBinding.score.text = nowScore.toString()

        if (nowScore > bestScore){
            MMKVManager.bestScore = nowScore
            viewBinding.bestScore.text = nowScore.toString()
        }
        else{
            viewBinding.bestScore.text = bestScore.toString()
        }

    }
}