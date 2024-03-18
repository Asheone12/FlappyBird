package com.muen.flappybird.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.muen.flappybird.R
import com.muen.flappybird.databinding.ActivityGameBinding
import java.util.Timer
import kotlin.concurrent.schedule

class GameActivity : AppCompatActivity() {
    private lateinit var ulas : ActivityGameBinding
    //Burak Karagöz

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


    override fun onCreate(savedInstanceState: Bundle?) {
        ulas = ActivityGameBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(ulas.root)

        ulas.cl.setOnTouchListener(object : View.OnTouchListener{
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
        ulas.imgfvBird.y=birdY
    }

    fun birdUcmaAnimasyonu(){
        val ucmaAnimasyonu = ObjectAnimator.ofFloat(ulas.imgfvBird,"rotation",-25.0f,0.0f).apply {
        }
        ucmaAnimasyonu.start()
    }

    fun baslangicYaziSilme(){
        val alphAnimasyonu = ObjectAnimator.ofFloat(ulas.txtTips,"alpha",1.0f,0.0f).apply {
            duration = 500
        }
        alphAnimasyonu.start()
    }

    fun konumVeBoyutBilgileri(){
        birdX = ulas.imgfvBird.x
        birdY = ulas.imgfvBird.y

        pipeUstX = ulas.imgfvPipeTopL.x
        pipeUstY = ulas.imgfvPipeTopL.y
        pipeAltX = ulas.imgfvPipeBottomL.x
        pipeAltY = ulas.imgfvPipeBottomL.y
        pipe2AltX = ulas.imgfvPipeBottomS.x
        pipe2AltY = ulas.imgfvPipeBottomS.y
        pipe2UstX = ulas.imgfvPipeTopS.x
        pipe2UstY = ulas.imgfvPipeTopS.y


        ekranYuksekligi = ulas.cl.height
        ekranGenisligi = ulas.cl.width
        birdYuksekligi = ulas.imgfvBird.height
        birdGenisligi = ulas.imgfvBird.width

        pipeUstYukseklik = ulas.imgfvPipeTopL.height
        pipeUstGenislik = ulas.imgfvPipeTopL.width
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

        ulas.imgfvPipeTopL.x = pipeUstX
        ulas.imgfvPipeBottomL.x = pipeAltX

        ulas.imgfvPipeTopL.y = pipeUstY
        ulas.imgfvPipeBottomL.y = pipeAltY


        pipe2UstX-=15.0f
        pipe2AltX-=15.0f

        if(pipe2UstX < -pipeUstGenislik && pipe2AltX < -pipeUstGenislik){
            pipe2UstX = (ekranGenisligi +pipeUstGenislik).toFloat()
            pipe2AltX = (ekranGenisligi +pipeUstGenislik).toFloat()

            val cikacakSayi = ekranYuksekligi - pipeUstYukseklik

            pipe2UstY = ((birdYuksekligi..ekranYuksekligi-cikacakSayi).random()-pipeUstYukseklik).toFloat()
            pipe2AltY = (pipe2UstY+pipeUstYukseklik+(birdYuksekligi*3))
        }

        ulas.imgfvPipeTopS.x=pipe2UstX
        ulas.imgfvPipeBottomS.x=pipe2AltX

        ulas.imgfvPipeTopS.y = pipe2UstY
        ulas.imgfvPipeBottomS.y = pipe2AltY
    }

    fun carpismaTesit(){
        var bSagUx = ulas.imgfvBird.x+birdGenisligi
        var bSagUy = ulas.imgfvBird.y
        var bSolUx = ulas.imgfvBird.x
        var bSolUy = ulas.imgfvBird.y

        var bSagAx = ulas.imgfvBird.x+birdGenisligi
        var bSagAy = ulas.imgfvBird.y + birdYuksekligi
        var bSolAx = ulas.imgfvBird.x
        var bSolAy = ulas.imgfvBird.y + birdYuksekligi

        var pipeOrtaNokta = ulas.imgfvPipeTopL.x + ulas.imgfvPipeTopL.width/2
        var pipe2OrtaNokta = ulas.imgfvPipeTopS.x + ulas.imgfvPipeTopS.width/2
        var birdOrtaNokta = ulas.imgfvBird.x + ulas.imgfvBird.width/2



        if (bSagUy >= ulas.imgfvPipeTopL.y && bSagUy <= ulas.imgfvPipeTopL.y + pipeUstYukseklik &&
            bSolUy >= ulas.imgfvPipeTopL.y && bSolUy <= ulas.imgfvPipeTopL.y + pipeUstYukseklik &&
            bSagUx >= ulas.imgfvPipeTopL.x && bSagUx <= ulas.imgfvPipeTopL.x + pipeUstGenislik &&
            bSolUx >= ulas.imgfvPipeTopL.x && bSolUx <= ulas.imgfvPipeTopL.x + pipeUstGenislik
            ||
            bSagUy >= ulas.imgfvPipeTopS.y && bSagUy <= ulas.imgfvPipeTopS.y + pipeUstYukseklik &&
            bSolUy >= ulas.imgfvPipeTopS.y && bSolUy <= ulas.imgfvPipeTopS.y + pipeUstYukseklik &&
            bSagUx >= ulas.imgfvPipeTopS.x && bSagUx <= ulas.imgfvPipeTopS.x + pipeUstGenislik &&
            bSolUx >= ulas.imgfvPipeTopS.x && bSolUx <= ulas.imgfvPipeTopS.x + pipeUstGenislik
            ||
            bSagAy >= ulas.imgfvPipeBottomL.y && bSagAy <= ulas.imgfvPipeBottomL.y + pipeUstYukseklik &&
            bSolAy >= ulas.imgfvPipeBottomL.y && bSolAy <= ulas.imgfvPipeBottomL.y + pipeUstYukseklik &&
            bSagAx >= ulas.imgfvPipeBottomL.x && bSagAx <= ulas.imgfvPipeBottomL.x + pipeUstGenislik &&
            bSolAx >= ulas.imgfvPipeBottomL.x && bSolAx <= ulas.imgfvPipeBottomL.x + pipeUstGenislik
            ||
            bSagAy >= ulas.imgfvPipeBottomS.y && bSagAy <= ulas.imgfvPipeBottomS.y + pipeUstYukseklik &&
            bSolAy >= ulas.imgfvPipeBottomS.y && bSolAy <= ulas.imgfvPipeBottomS.y + pipeUstYukseklik &&
            bSagAx >= ulas.imgfvPipeBottomS.x && bSagAx <= ulas.imgfvPipeBottomS.x + pipeUstGenislik &&
            bSolAx >= ulas.imgfvPipeBottomS.x && bSolAx <= ulas.imgfvPipeBottomS.x + pipeUstGenislik){

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
            ulas.txtScore.text = skor.toString()
            pipe1PuanKontrol = false
            pipe2PuanKontrol = true
        }
        if(birdOrtaNokta >= pipe2OrtaNokta && pipe2PuanKontrol){
            muzukcalar = MediaPlayer.create(this, R.raw.puan)
            muzukcalar.start()
            skor+=1
            ulas.txtScore.text = skor.toString()
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