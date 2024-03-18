package com.muen.flappybird.ui

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import com.muen.flappybird.R
import com.muen.flappybird.databinding.ActivityGameBinding
import com.muen.flappybird.util.BaseActivity
import java.util.Timer
import kotlin.concurrent.schedule

class GameActivity : BaseActivity<ActivityGameBinding>() {
    //位置
    private var birdX= 0.0f
    private var birdY= 0.0f
    private var pipeTopLargeX = 0.0f
    private var pipeTopLargeY = 0.0f
    private var pipeBottomLargeX = 0.0f
    private var pipeBottomLargeY = 0.0f
    private var pipeTopSmallX = 0.0f
    private var pipeTopSmallY = 0.0f
    private var pipeBottomSmallX = 0.0f
    private var pipeBottomSmallY = 0.0f

    //控制器
    private var touchControl = false
    private var startControl = false
    var pipe1PuanControl = true
    var pipe2PuanControl = true

    //屏幕参数
    private var screenWidth = 0
    private var screenHeight = 0
    private var birdWidth = 0
    private var birdHeight = 0
    private var pipeTopWidth = 0
    private var pipeTopHeight = 0

    private val timer = Timer()
    private var score = 0
    private var mediaPlayer = MediaPlayer()

    override fun onCreateViewBinding(): ActivityGameBinding {
        return ActivityGameBinding.inflate(layoutInflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initListener() {
        super.initListener()

        viewBinding.view.setOnTouchListener { _, event ->
            if (startControl) {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    birdFlyAnimator()
                    touchControl = true
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    touchControl = false
                }
            } else {
                startControl = true
                removeTips()
                updateLocation()

                timer.schedule(0, 30) {
                    Handler(Looper.getMainLooper()).post {
                        birdMove()
                        pipeMove()
                        collide()
                    }
                }
            }
            true
        }
    }

    private fun birdMove(){
        if (touchControl){
            touchControl=false
            birdY-=birdHeight+birdHeight/5
        }else{
            birdY+=birdHeight/8
        }

        if(birdY<=0.0f){
            birdY=0.0f
        }
        if(birdY >= screenHeight-birdHeight){
            birdY =(screenHeight-birdHeight).toFloat()
        }
        viewBinding.bird.y=birdY
    }

    private fun birdFlyAnimator(){
        val flyAnimator = ObjectAnimator.ofFloat(viewBinding.bird,"rotation",-25.0f,0.0f).apply {
        }
        flyAnimator.start()
    }

    private fun removeTips(){
        val alphaAnimator = ObjectAnimator.ofFloat(viewBinding.txtTips,"alpha",1.0f,0.0f).apply {
            duration = 500
        }
        alphaAnimator.start()
    }

    private fun updateLocation(){
        birdX = viewBinding.bird.x
        birdY = viewBinding.bird.y

        pipeTopLargeX = viewBinding.pipeTopLarge.x
        pipeTopLargeY = viewBinding.pipeTopLarge.y
        pipeBottomLargeX = viewBinding.pipeBottomLarge.x
        pipeBottomLargeY = viewBinding.pipeBottomLarge.y
        pipeBottomSmallX = viewBinding.pipeBottomSmall.x
        pipeBottomSmallY = viewBinding.pipeBottomSmall.y
        pipeTopSmallX = viewBinding.pipeTopSmall.x
        pipeTopSmallY = viewBinding.pipeTopSmall.y


        screenHeight = viewBinding.view.height
        screenWidth = viewBinding.view.width
        birdHeight = viewBinding.bird.height
        birdWidth = viewBinding.bird.width

        pipeTopHeight = viewBinding.pipeTopLarge.height
        pipeTopWidth = viewBinding.pipeTopLarge.width
    }

    private fun pipeMove(){

        pipeTopLargeX-=15.0f
        pipeBottomLargeX-=15.0f

        if (pipeTopLargeX < -pipeTopWidth && pipeBottomLargeX < -pipeTopWidth){
            pipeTopLargeX = (screenWidth +pipeTopWidth).toFloat()
            pipeBottomLargeX = (screenWidth +pipeTopWidth).toFloat()

            val cikacakSayi = screenHeight - pipeTopHeight

            pipeTopLargeY = ((birdHeight..screenHeight-cikacakSayi).random()-pipeTopHeight).toFloat()
            pipeBottomLargeY = (pipeTopLargeY+pipeTopHeight+(birdHeight*3))
        }

        viewBinding.pipeTopLarge.x = pipeTopLargeX
        viewBinding.pipeBottomLarge.x = pipeBottomLargeX

        viewBinding.pipeTopLarge.y = pipeTopLargeY
        viewBinding.pipeBottomLarge.y = pipeBottomLargeY


        pipeTopSmallX-=15.0f
        pipeBottomSmallX-=15.0f

        if(pipeTopSmallX < -pipeTopWidth && pipeBottomSmallX < -pipeTopWidth){
            pipeTopSmallX = (screenWidth +pipeTopWidth).toFloat()
            pipeBottomSmallX = (screenWidth +pipeTopWidth).toFloat()

            val cikacakSayi = screenHeight - pipeTopHeight

            pipeTopSmallY = ((birdHeight..screenHeight-cikacakSayi).random()-pipeTopHeight).toFloat()
            pipeBottomSmallY = (pipeTopSmallY+pipeTopHeight+(birdHeight*3))
        }

        viewBinding.pipeTopSmall.x=pipeTopSmallX
        viewBinding.pipeBottomSmall.x=pipeBottomSmallX

        viewBinding.pipeTopSmall.y = pipeTopSmallY
        viewBinding.pipeBottomSmall.y = pipeBottomSmallY
    }

    private fun collide(){
        var bSagUx = viewBinding.bird.x+birdWidth
        var bSagUy = viewBinding.bird.y
        var bSolUx = viewBinding.bird.x
        var bSolUy = viewBinding.bird.y

        var bSagAx = viewBinding.bird.x+birdWidth
        var bSagAy = viewBinding.bird.y + birdHeight
        var bSolAx = viewBinding.bird.x
        var bSolAy = viewBinding.bird.y + birdHeight

        var pipeMidpoint = viewBinding.pipeTopLarge.x + viewBinding.pipeTopLarge.width/2
        var pipe2Midpoint = viewBinding.pipeTopSmall.x + viewBinding.pipeTopSmall.width/2
        var birdMidpoint = viewBinding.bird.x + viewBinding.bird.width/2



        if (bSagUy >= viewBinding.pipeTopLarge.y && bSagUy <= viewBinding.pipeTopLarge.y + pipeTopHeight &&
            bSolUy >= viewBinding.pipeTopLarge.y && bSolUy <= viewBinding.pipeTopLarge.y + pipeTopHeight &&
            bSagUx >= viewBinding.pipeTopLarge.x && bSagUx <= viewBinding.pipeTopLarge.x + pipeTopWidth &&
            bSolUx >= viewBinding.pipeTopLarge.x && bSolUx <= viewBinding.pipeTopLarge.x + pipeTopWidth
            ||
            bSagUy >= viewBinding.pipeTopSmall.y && bSagUy <= viewBinding.pipeTopSmall.y + pipeTopHeight &&
            bSolUy >= viewBinding.pipeTopSmall.y && bSolUy <= viewBinding.pipeTopSmall.y + pipeTopHeight &&
            bSagUx >= viewBinding.pipeTopSmall.x && bSagUx <= viewBinding.pipeTopSmall.x + pipeTopWidth &&
            bSolUx >= viewBinding.pipeTopSmall.x && bSolUx <= viewBinding.pipeTopSmall.x + pipeTopWidth
            ||
            bSagAy >= viewBinding.pipeBottomLarge.y && bSagAy <= viewBinding.pipeBottomLarge.y + pipeTopHeight &&
            bSolAy >= viewBinding.pipeBottomLarge.y && bSolAy <= viewBinding.pipeBottomLarge.y + pipeTopHeight &&
            bSagAx >= viewBinding.pipeBottomLarge.x && bSagAx <= viewBinding.pipeBottomLarge.x + pipeTopWidth &&
            bSolAx >= viewBinding.pipeBottomLarge.x && bSolAx <= viewBinding.pipeBottomLarge.x + pipeTopWidth
            ||
            bSagAy >= viewBinding.pipeBottomSmall.y && bSagAy <= viewBinding.pipeBottomSmall.y + pipeTopHeight &&
            bSolAy >= viewBinding.pipeBottomSmall.y && bSolAy <= viewBinding.pipeBottomSmall.y + pipeTopHeight &&
            bSagAx >= viewBinding.pipeBottomSmall.x && bSagAx <= viewBinding.pipeBottomSmall.x + pipeTopWidth &&
            bSolAx >= viewBinding.pipeBottomSmall.x && bSolAx <= viewBinding.pipeBottomSmall.x + pipeTopWidth){

            timer.cancel()
            mediaPlayer = MediaPlayer.create(this, R.raw.carpma)
            mediaPlayer.start()
            val intent = Intent(this@GameActivity, ResultActivity::class.java)
            intent.putExtra("score",score)
            startActivity(intent)
            finish()
        }

        if (birdMidpoint >= pipeMidpoint && pipe1PuanControl){
            mediaPlayer = MediaPlayer.create(this, R.raw.puan)
            mediaPlayer.start()
            score+=1
            viewBinding.txtScore.text = score.toString()
            pipe1PuanControl = false
            pipe2PuanControl = true
        }
        if(birdMidpoint >= pipe2Midpoint && pipe2PuanControl){
            mediaPlayer = MediaPlayer.create(this, R.raw.puan)
            mediaPlayer.start()
            score+=1
            viewBinding.txtScore.text = score.toString()
            pipe1PuanControl = true
            pipe2PuanControl = false
        }

    }

    override fun onBackPressed() {
        finish()
        timer.cancel()
        super.onBackPressed()
    }
}