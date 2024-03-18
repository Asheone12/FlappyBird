package com.muen.flappybird.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import com.muen.flappybird.R
import com.muen.flappybird.databinding.ActivityGameBinding
import com.muen.flappybird.util.BaseActivity
import java.util.Timer
import kotlin.concurrent.schedule

class GameActivity : BaseActivity<ActivityGameBinding>() {
    //位置
    private var birdX= 0.0f
    private var birdY= 0.0f
    private var pipeUstX = 0.0f
    private var pipeUstY = 0.0f
    private var pipeAltX = 0.0f
    private var pipeAltY = 0.0f
    private var pipe2UstX = 0.0f
    private var pipe2UstY = 0.0f
    private var pipe2AltX = 0.0f
    private var pipe2AltY = 0.0f

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

    override fun initListener() {
        super.initListener()

        viewBinding.cl.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (startControl){
                    if(event?.action == MotionEvent.ACTION_DOWN){
                        birdFlyAnimator()
                        touchControl = true
                    }
                    if (event?.action == MotionEvent.ACTION_UP){
                        touchControl = false
                    }
                } else{
                    startControl = true
                    removeTips()
                    updateLocation()

                    timer.schedule(0,30){
                        Handler(Looper.getMainLooper()).post{
                            birdMove()
                            pipeMove()
                            collide()
                        }
                    }
                }


                return true
            }
        })
    }

    fun birdMove(){
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
        viewBinding.imgfvBird.y=birdY
    }

    fun birdFlyAnimator(){
        val flyAnimator = ObjectAnimator.ofFloat(viewBinding.imgfvBird,"rotation",-25.0f,0.0f).apply {
        }
        flyAnimator.start()
    }

    fun removeTips(){
        val alphaAnimator = ObjectAnimator.ofFloat(viewBinding.txtTips,"alpha",1.0f,0.0f).apply {
            duration = 500
        }
        alphaAnimator.start()
    }

    fun updateLocation(){
        birdX = viewBinding.imgfvBird.x
        birdY = viewBinding.imgfvBird.y

        pipeUstX = viewBinding.imgfvPipeTopL.x
        pipeUstY = viewBinding.imgfvPipeTopL.y
        pipeAltX = viewBinding.imgfvPipeBottomL.x
        pipeAltY = viewBinding.imgfvPipeBottomL.y
        pipe2AltX = viewBinding.imgfvPipeBottomS.x
        pipe2AltY = viewBinding.imgfvPipeBottomS.y
        pipe2UstX = viewBinding.imgfvPipeTopS.x
        pipe2UstY = viewBinding.imgfvPipeTopS.y


        screenHeight = viewBinding.cl.height
        screenWidth = viewBinding.cl.width
        birdHeight = viewBinding.imgfvBird.height
        birdWidth = viewBinding.imgfvBird.width

        pipeTopHeight = viewBinding.imgfvPipeTopL.height
        pipeTopWidth = viewBinding.imgfvPipeTopL.width
    }

    fun pipeMove(){

        pipeUstX-=15.0f
        pipeAltX-=15.0f

        if (pipeUstX < -pipeTopWidth && pipeAltX < -pipeTopWidth){
            pipeUstX = (screenWidth +pipeTopWidth).toFloat()
            pipeAltX = (screenWidth +pipeTopWidth).toFloat()

            val cikacakSayi = screenHeight - pipeTopHeight

            pipeUstY = ((birdHeight..screenHeight-cikacakSayi).random()-pipeTopHeight).toFloat()
            pipeAltY = (pipeUstY+pipeTopHeight+(birdHeight*3))
        }

        viewBinding.imgfvPipeTopL.x = pipeUstX
        viewBinding.imgfvPipeBottomL.x = pipeAltX

        viewBinding.imgfvPipeTopL.y = pipeUstY
        viewBinding.imgfvPipeBottomL.y = pipeAltY


        pipe2UstX-=15.0f
        pipe2AltX-=15.0f

        if(pipe2UstX < -pipeTopWidth && pipe2AltX < -pipeTopWidth){
            pipe2UstX = (screenWidth +pipeTopWidth).toFloat()
            pipe2AltX = (screenWidth +pipeTopWidth).toFloat()

            val cikacakSayi = screenHeight - pipeTopHeight

            pipe2UstY = ((birdHeight..screenHeight-cikacakSayi).random()-pipeTopHeight).toFloat()
            pipe2AltY = (pipe2UstY+pipeTopHeight+(birdHeight*3))
        }

        viewBinding.imgfvPipeTopS.x=pipe2UstX
        viewBinding.imgfvPipeBottomS.x=pipe2AltX

        viewBinding.imgfvPipeTopS.y = pipe2UstY
        viewBinding.imgfvPipeBottomS.y = pipe2AltY
    }

    fun collide(){
        var bSagUx = viewBinding.imgfvBird.x+birdWidth
        var bSagUy = viewBinding.imgfvBird.y
        var bSolUx = viewBinding.imgfvBird.x
        var bSolUy = viewBinding.imgfvBird.y

        var bSagAx = viewBinding.imgfvBird.x+birdWidth
        var bSagAy = viewBinding.imgfvBird.y + birdHeight
        var bSolAx = viewBinding.imgfvBird.x
        var bSolAy = viewBinding.imgfvBird.y + birdHeight

        var pipeMidpoint = viewBinding.imgfvPipeTopL.x + viewBinding.imgfvPipeTopL.width/2
        var pipe2Midpoint = viewBinding.imgfvPipeTopS.x + viewBinding.imgfvPipeTopS.width/2
        var birdMidpoint = viewBinding.imgfvBird.x + viewBinding.imgfvBird.width/2



        if (bSagUy >= viewBinding.imgfvPipeTopL.y && bSagUy <= viewBinding.imgfvPipeTopL.y + pipeTopHeight &&
            bSolUy >= viewBinding.imgfvPipeTopL.y && bSolUy <= viewBinding.imgfvPipeTopL.y + pipeTopHeight &&
            bSagUx >= viewBinding.imgfvPipeTopL.x && bSagUx <= viewBinding.imgfvPipeTopL.x + pipeTopWidth &&
            bSolUx >= viewBinding.imgfvPipeTopL.x && bSolUx <= viewBinding.imgfvPipeTopL.x + pipeTopWidth
            ||
            bSagUy >= viewBinding.imgfvPipeTopS.y && bSagUy <= viewBinding.imgfvPipeTopS.y + pipeTopHeight &&
            bSolUy >= viewBinding.imgfvPipeTopS.y && bSolUy <= viewBinding.imgfvPipeTopS.y + pipeTopHeight &&
            bSagUx >= viewBinding.imgfvPipeTopS.x && bSagUx <= viewBinding.imgfvPipeTopS.x + pipeTopWidth &&
            bSolUx >= viewBinding.imgfvPipeTopS.x && bSolUx <= viewBinding.imgfvPipeTopS.x + pipeTopWidth
            ||
            bSagAy >= viewBinding.imgfvPipeBottomL.y && bSagAy <= viewBinding.imgfvPipeBottomL.y + pipeTopHeight &&
            bSolAy >= viewBinding.imgfvPipeBottomL.y && bSolAy <= viewBinding.imgfvPipeBottomL.y + pipeTopHeight &&
            bSagAx >= viewBinding.imgfvPipeBottomL.x && bSagAx <= viewBinding.imgfvPipeBottomL.x + pipeTopWidth &&
            bSolAx >= viewBinding.imgfvPipeBottomL.x && bSolAx <= viewBinding.imgfvPipeBottomL.x + pipeTopWidth
            ||
            bSagAy >= viewBinding.imgfvPipeBottomS.y && bSagAy <= viewBinding.imgfvPipeBottomS.y + pipeTopHeight &&
            bSolAy >= viewBinding.imgfvPipeBottomS.y && bSolAy <= viewBinding.imgfvPipeBottomS.y + pipeTopHeight &&
            bSagAx >= viewBinding.imgfvPipeBottomS.x && bSagAx <= viewBinding.imgfvPipeBottomS.x + pipeTopWidth &&
            bSolAx >= viewBinding.imgfvPipeBottomS.x && bSolAx <= viewBinding.imgfvPipeBottomS.x + pipeTopWidth){

            timer.cancel()
            mediaPlayer = MediaPlayer.create(this, R.raw.carpma)
            mediaPlayer.start()
            val intent = Intent(this@GameActivity, ResultActivity::class.java)
            intent.putExtra("skor",score)
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