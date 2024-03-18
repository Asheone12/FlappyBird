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
    //pozisyonlar
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

    //kontroller
    private var dokunmaKontrol = false
    private var baslangicKontrol = false
    var pipe1PuanKontrol = true
    var pipe2PuanKontrol = true

    //durumlar
    private var ekranGenisligi = 0
    private var ekranYuksekligi = 0
    private var birdGenisligi = 0
    private var birdYuksekligi = 0
    private var pipeUstYukseklik = 0
    private var pipeUstGenislik = 0

    private val timer = Timer()
    private var skor = 0
    private var muzukcalar = MediaPlayer()

    override fun onCreateViewBinding(): ActivityGameBinding {
        return ActivityGameBinding.inflate(layoutInflater)
    }

    override fun initListener() {
        super.initListener()

        viewBinding.cl.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (baslangicKontrol){
                    if(event?.action == MotionEvent.ACTION_DOWN){
                        birdUcmaAnimasyonu()
                        dokunmaKontrol = true
                    }
                    if (event?.action == MotionEvent.ACTION_UP){
                        dokunmaKontrol = false
                    }
                } else{
                    baslangicKontrol = true
                    baslangicYaziSilme()
                    konumVeBoyutBilgileri()

                    timer.schedule(0,30){
                        Handler(Looper.getMainLooper()).post{
                            birdHareketi()
                            pipeHareketi()
                            carpismaTesit()
                        }
                    }
                }


                return true
            }
        })
    }

    fun birdHareketi(){
        if (dokunmaKontrol){
            dokunmaKontrol=false
            birdY-=birdYuksekligi+birdYuksekligi/5
        }else{
            birdY+=birdYuksekligi/8
        }

        if(birdY<=0.0f){
            birdY=0.0f
        }
        if(birdY >= ekranYuksekligi-birdYuksekligi){
            birdY =(ekranYuksekligi-birdYuksekligi).toFloat()
        }
        viewBinding.imgfvBird.y=birdY
    }

    fun birdUcmaAnimasyonu(){
        val ucmaAnimasyonu = ObjectAnimator.ofFloat(viewBinding.imgfvBird,"rotation",-25.0f,0.0f).apply {
        }
        ucmaAnimasyonu.start()
    }

    fun baslangicYaziSilme(){
        val alphAnimasyonu = ObjectAnimator.ofFloat(viewBinding.txtTips,"alpha",1.0f,0.0f).apply {
            duration = 500
        }
        alphAnimasyonu.start()
    }

    fun konumVeBoyutBilgileri(){
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


        ekranYuksekligi = viewBinding.cl.height
        ekranGenisligi = viewBinding.cl.width
        birdYuksekligi = viewBinding.imgfvBird.height
        birdGenisligi = viewBinding.imgfvBird.width

        pipeUstYukseklik = viewBinding.imgfvPipeTopL.height
        pipeUstGenislik = viewBinding.imgfvPipeTopL.width
    }

    fun pipeHareketi(){

        pipeUstX-=15.0f
        pipeAltX-=15.0f

        if (pipeUstX < -pipeUstGenislik && pipeAltX < -pipeUstGenislik){
            pipeUstX = (ekranGenisligi +pipeUstGenislik).toFloat()
            pipeAltX = (ekranGenisligi +pipeUstGenislik).toFloat()

            val cikacakSayi = ekranYuksekligi - pipeUstYukseklik

            pipeUstY = ((birdYuksekligi..ekranYuksekligi-cikacakSayi).random()-pipeUstYukseklik).toFloat()
            pipeAltY = (pipeUstY+pipeUstYukseklik+(birdYuksekligi*3))
        }

        viewBinding.imgfvPipeTopL.x = pipeUstX
        viewBinding.imgfvPipeBottomL.x = pipeAltX

        viewBinding.imgfvPipeTopL.y = pipeUstY
        viewBinding.imgfvPipeBottomL.y = pipeAltY


        pipe2UstX-=15.0f
        pipe2AltX-=15.0f

        if(pipe2UstX < -pipeUstGenislik && pipe2AltX < -pipeUstGenislik){
            pipe2UstX = (ekranGenisligi +pipeUstGenislik).toFloat()
            pipe2AltX = (ekranGenisligi +pipeUstGenislik).toFloat()

            val cikacakSayi = ekranYuksekligi - pipeUstYukseklik

            pipe2UstY = ((birdYuksekligi..ekranYuksekligi-cikacakSayi).random()-pipeUstYukseklik).toFloat()
            pipe2AltY = (pipe2UstY+pipeUstYukseklik+(birdYuksekligi*3))
        }

        viewBinding.imgfvPipeTopS.x=pipe2UstX
        viewBinding.imgfvPipeBottomS.x=pipe2AltX

        viewBinding.imgfvPipeTopS.y = pipe2UstY
        viewBinding.imgfvPipeBottomS.y = pipe2AltY
    }

    fun carpismaTesit(){
        var bSagUx = viewBinding.imgfvBird.x+birdGenisligi
        var bSagUy = viewBinding.imgfvBird.y
        var bSolUx = viewBinding.imgfvBird.x
        var bSolUy = viewBinding.imgfvBird.y

        var bSagAx = viewBinding.imgfvBird.x+birdGenisligi
        var bSagAy = viewBinding.imgfvBird.y + birdYuksekligi
        var bSolAx = viewBinding.imgfvBird.x
        var bSolAy = viewBinding.imgfvBird.y + birdYuksekligi

        var pipeOrtaNokta = viewBinding.imgfvPipeTopL.x + viewBinding.imgfvPipeTopL.width/2
        var pipe2OrtaNokta = viewBinding.imgfvPipeTopS.x + viewBinding.imgfvPipeTopS.width/2
        var birdOrtaNokta = viewBinding.imgfvBird.x + viewBinding.imgfvBird.width/2



        if (bSagUy >= viewBinding.imgfvPipeTopL.y && bSagUy <= viewBinding.imgfvPipeTopL.y + pipeUstYukseklik &&
            bSolUy >= viewBinding.imgfvPipeTopL.y && bSolUy <= viewBinding.imgfvPipeTopL.y + pipeUstYukseklik &&
            bSagUx >= viewBinding.imgfvPipeTopL.x && bSagUx <= viewBinding.imgfvPipeTopL.x + pipeUstGenislik &&
            bSolUx >= viewBinding.imgfvPipeTopL.x && bSolUx <= viewBinding.imgfvPipeTopL.x + pipeUstGenislik
            ||
            bSagUy >= viewBinding.imgfvPipeTopS.y && bSagUy <= viewBinding.imgfvPipeTopS.y + pipeUstYukseklik &&
            bSolUy >= viewBinding.imgfvPipeTopS.y && bSolUy <= viewBinding.imgfvPipeTopS.y + pipeUstYukseklik &&
            bSagUx >= viewBinding.imgfvPipeTopS.x && bSagUx <= viewBinding.imgfvPipeTopS.x + pipeUstGenislik &&
            bSolUx >= viewBinding.imgfvPipeTopS.x && bSolUx <= viewBinding.imgfvPipeTopS.x + pipeUstGenislik
            ||
            bSagAy >= viewBinding.imgfvPipeBottomL.y && bSagAy <= viewBinding.imgfvPipeBottomL.y + pipeUstYukseklik &&
            bSolAy >= viewBinding.imgfvPipeBottomL.y && bSolAy <= viewBinding.imgfvPipeBottomL.y + pipeUstYukseklik &&
            bSagAx >= viewBinding.imgfvPipeBottomL.x && bSagAx <= viewBinding.imgfvPipeBottomL.x + pipeUstGenislik &&
            bSolAx >= viewBinding.imgfvPipeBottomL.x && bSolAx <= viewBinding.imgfvPipeBottomL.x + pipeUstGenislik
            ||
            bSagAy >= viewBinding.imgfvPipeBottomS.y && bSagAy <= viewBinding.imgfvPipeBottomS.y + pipeUstYukseklik &&
            bSolAy >= viewBinding.imgfvPipeBottomS.y && bSolAy <= viewBinding.imgfvPipeBottomS.y + pipeUstYukseklik &&
            bSagAx >= viewBinding.imgfvPipeBottomS.x && bSagAx <= viewBinding.imgfvPipeBottomS.x + pipeUstGenislik &&
            bSolAx >= viewBinding.imgfvPipeBottomS.x && bSolAx <= viewBinding.imgfvPipeBottomS.x + pipeUstGenislik){

            timer.cancel()
            muzukcalar = MediaPlayer.create(this, R.raw.carpma)
            muzukcalar.start()
            val intent = Intent(this@GameActivity, ResultActivity::class.java)
            intent.putExtra("skor",skor)
            startActivity(intent)
            finish()
        }

        if (birdOrtaNokta >= pipeOrtaNokta && pipe1PuanKontrol){
            muzukcalar = MediaPlayer.create(this, R.raw.puan)
            muzukcalar.start()
            skor+=1
            viewBinding.txtScore.text = skor.toString()
            pipe1PuanKontrol = false
            pipe2PuanKontrol = true
        }
        if(birdOrtaNokta >= pipe2OrtaNokta && pipe2PuanKontrol){
            muzukcalar = MediaPlayer.create(this, R.raw.puan)
            muzukcalar.start()
            skor+=1
            viewBinding.txtScore.text = skor.toString()
            pipe1PuanKontrol = true
            pipe2PuanKontrol = false
        }



    }

    override fun onBackPressed() {
        finish()
        timer.cancel()
        super.onBackPressed()
    }
}