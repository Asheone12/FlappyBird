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
    private var pipeTop1X = 0.0f
    private var pipeTop1Y = 0.0f
    private var pipeBottom1X = 0.0f
    private var pipeBottom1Y = 0.0f
    private var pipeTop2X = 0.0f
    private var pipeTop2Y = 0.0f
    private var pipeBottom2X = 0.0f
    private var pipeBottom2Y = 0.0f

    //控制器
    private var touchControl = false
    private var startControl = false
    private var pipe1PassControl = true
    private var pipe2PassControl = true

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

        pipeTop1X = viewBinding.pipeTop1.x
        pipeTop1Y = viewBinding.pipeTop1.y
        pipeBottom1X = viewBinding.pipeBottom1.x
        pipeBottom1Y = viewBinding.pipeBottom1.y
        pipeBottom2X = viewBinding.pipeBottom2.x
        pipeBottom2Y = viewBinding.pipeBottom2.y
        pipeTop2X = viewBinding.pipeTop2.x
        pipeTop2Y = viewBinding.pipeTop2.y


        screenHeight = viewBinding.view.height
        screenWidth = viewBinding.view.width
        birdHeight = viewBinding.bird.height
        birdWidth = viewBinding.bird.width

        pipeTopHeight = viewBinding.pipeTop1.height
        pipeTopWidth = viewBinding.pipeTop1.width
    }

    private fun pipeMove(){

        pipeTop1X-=15.0f
        pipeBottom1X-=15.0f

        if (pipeTop1X < -pipeTopWidth && pipeBottom1X < -pipeTopWidth){
            pipeTop1X = (screenWidth +pipeTopWidth).toFloat()
            pipeBottom1X = (screenWidth +pipeTopWidth).toFloat()

            val leastHeight = screenHeight - pipeTopHeight

            pipeTop1Y = ((birdHeight..screenHeight-leastHeight).random()-pipeTopHeight).toFloat()
            pipeBottom1Y = (pipeTop1Y+pipeTopHeight+(birdHeight*3))
        }

        viewBinding.pipeTop1.x = pipeTop1X
        viewBinding.pipeBottom1.x = pipeBottom1X

        viewBinding.pipeTop1.y = pipeTop1Y
        viewBinding.pipeBottom1.y = pipeBottom1Y


        pipeTop2X-=15.0f
        pipeBottom2X-=15.0f

        if(pipeTop2X < -pipeTopWidth && pipeBottom2X < -pipeTopWidth){
            pipeTop2X = (screenWidth +pipeTopWidth).toFloat()
            pipeBottom2X = (screenWidth +pipeTopWidth).toFloat()

            val leastHeight = screenHeight - pipeTopHeight

            pipeTop2Y = ((birdHeight..screenHeight-leastHeight).random()-pipeTopHeight).toFloat()
            pipeBottom2Y = (pipeTop2Y+pipeTopHeight+(birdHeight*3))
        }

        viewBinding.pipeTop2.x=pipeTop2X
        viewBinding.pipeBottom2.x=pipeBottom2X

        viewBinding.pipeTop2.y = pipeTop2Y
        viewBinding.pipeBottom2.y = pipeBottom2Y
    }

    private fun collide(){
        val birdStartX = viewBinding.bird.x
        val birdStartY = viewBinding.bird.y
        val birdEndX = viewBinding.bird.x+birdWidth
        val birdEndY = viewBinding.bird.y + birdHeight

        val pipe1Midpoint = viewBinding.pipeTop1.x + viewBinding.pipeTop1.width/2
        val pipe2Midpoint = viewBinding.pipeTop2.x + viewBinding.pipeTop2.width/2
        val birdMidpoint = viewBinding.bird.x + viewBinding.bird.width/2



        if (birdStartY >= viewBinding.pipeTop1.y && birdStartY <= viewBinding.pipeTop1.y + pipeTopHeight &&
            birdStartY >= viewBinding.pipeTop1.y && birdStartY <= viewBinding.pipeTop1.y + pipeTopHeight &&
            birdEndX >= viewBinding.pipeTop1.x && birdEndX <= viewBinding.pipeTop1.x + pipeTopWidth &&
            birdStartX >= viewBinding.pipeTop1.x && birdStartX <= viewBinding.pipeTop1.x + pipeTopWidth
            ||
            birdStartY >= viewBinding.pipeTop2.y && birdStartY <= viewBinding.pipeTop2.y + pipeTopHeight &&
            birdStartY >= viewBinding.pipeTop2.y && birdStartY <= viewBinding.pipeTop2.y + pipeTopHeight &&
            birdEndX >= viewBinding.pipeTop2.x && birdEndX <= viewBinding.pipeTop2.x + pipeTopWidth &&
            birdStartX >= viewBinding.pipeTop2.x && birdStartX <= viewBinding.pipeTop2.x + pipeTopWidth
            ||
            birdEndY >= viewBinding.pipeBottom1.y && birdEndY <= viewBinding.pipeBottom1.y + pipeTopHeight &&
            birdEndY >= viewBinding.pipeBottom1.y && birdEndY <= viewBinding.pipeBottom1.y + pipeTopHeight &&
            birdEndX >= viewBinding.pipeBottom1.x && birdEndX <= viewBinding.pipeBottom1.x + pipeTopWidth &&
            birdStartX >= viewBinding.pipeBottom1.x && birdStartX <= viewBinding.pipeBottom1.x + pipeTopWidth
            ||
            birdEndY >= viewBinding.pipeBottom2.y && birdEndY <= viewBinding.pipeBottom2.y + pipeTopHeight &&
            birdEndY >= viewBinding.pipeBottom2.y && birdEndY <= viewBinding.pipeBottom2.y + pipeTopHeight &&
            birdEndX >= viewBinding.pipeBottom2.x && birdEndX <= viewBinding.pipeBottom2.x + pipeTopWidth &&
            birdStartX >= viewBinding.pipeBottom2.x && birdStartX <= viewBinding.pipeBottom2.x + pipeTopWidth){

            timer.cancel()
            mediaPlayer = MediaPlayer.create(this, R.raw.carpma)
            mediaPlayer.start()
            val intent = Intent(this@GameActivity, ResultActivity::class.java)
            intent.putExtra("score",score)
            startActivity(intent)
            finish()
        }

        if (birdMidpoint >= pipe1Midpoint && pipe1PassControl){
            mediaPlayer = MediaPlayer.create(this, R.raw.puan)
            mediaPlayer.start()
            score+=1
            viewBinding.txtScore.text = score.toString()
            pipe1PassControl = false
            pipe2PassControl = true
        }
        if(birdMidpoint >= pipe2Midpoint && pipe2PassControl){
            mediaPlayer = MediaPlayer.create(this, R.raw.puan)
            mediaPlayer.start()
            score+=1
            viewBinding.txtScore.text = score.toString()
            pipe1PassControl = true
            pipe2PassControl = false
        }

    }

    override fun onBackPressed() {
        finish()
        timer.cancel()
        super.onBackPressed()
    }
}