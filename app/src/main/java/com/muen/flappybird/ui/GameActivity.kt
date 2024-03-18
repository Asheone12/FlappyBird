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
        ulas.imageFViewBird.y=birdY
    }

    fun birdUcmaAnimasyonu(){
        val ucmaAnimasyonu = ObjectAnimator.ofFloat(ulas.imageFViewBird,"rotation",-25.0f,0.0f).apply {
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
        birdX = ulas.imageFViewBird.x
        birdY = ulas.imageFViewBird.y

        pipeUstX = ulas.imageFViewPust.x
        pipeUstY = ulas.imageFViewPust.y
        pipeAltX = ulas.imageFViewPalt.x
        pipeAltY = ulas.imageFViewPalt.y
        pipe2AltX = ulas.imageFViewPalt2.x
        pipe2AltY = ulas.imageFViewPalt2.y
        pipe2UstX = ulas.imageFViewPust2.x
        pipe2UstY = ulas.imageFViewPust2.y


        ekranYuksekligi = ulas.cl.height
        ekranGenisligi = ulas.cl.width
        birdYuksekligi = ulas.imageFViewBird.height
        birdGenisligi = ulas.imageFViewBird.width

        pipeUstYukseklik = ulas.imageFViewPust.height
        pipeUstGenislik = ulas.imageFViewPust.width
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

        ulas.imageFViewPust.x = pipeUstX
        ulas.imageFViewPalt.x = pipeAltX

        ulas.imageFViewPust.y = pipeUstY
        ulas.imageFViewPalt.y = pipeAltY


        pipe2UstX-=15.0f
        pipe2AltX-=15.0f

        if(pipe2UstX < -pipeUstGenislik && pipe2AltX < -pipeUstGenislik){
            pipe2UstX = (ekranGenisligi +pipeUstGenislik).toFloat()
            pipe2AltX = (ekranGenisligi +pipeUstGenislik).toFloat()

            val cikacakSayi = ekranYuksekligi - pipeUstYukseklik

            pipe2UstY = ((birdYuksekligi..ekranYuksekligi-cikacakSayi).random()-pipeUstYukseklik).toFloat()
            pipe2AltY = (pipe2UstY+pipeUstYukseklik+(birdYuksekligi*3))
        }

        ulas.imageFViewPust2.x=pipe2UstX
        ulas.imageFViewPalt2.x=pipe2AltX

        ulas.imageFViewPust2.y = pipe2UstY
        ulas.imageFViewPalt2.y = pipe2AltY
    }

    fun carpismaTesit(){
        var bSagUx = ulas.imageFViewBird.x+birdGenisligi
        var bSagUy = ulas.imageFViewBird.y
        var bSolUx = ulas.imageFViewBird.x
        var bSolUy = ulas.imageFViewBird.y

        var bSagAx = ulas.imageFViewBird.x+birdGenisligi
        var bSagAy = ulas.imageFViewBird.y + birdYuksekligi
        var bSolAx = ulas.imageFViewBird.x
        var bSolAy = ulas.imageFViewBird.y + birdYuksekligi

        var pipeOrtaNokta = ulas.imageFViewPust.x + ulas.imageFViewPust.width/2
        var pipe2OrtaNokta = ulas.imageFViewPust2.x + ulas.imageFViewPust2.width/2
        var birdOrtaNokta = ulas.imageFViewBird.x + ulas.imageFViewBird.width/2



        if (bSagUy >= ulas.imageFViewPust.y && bSagUy <= ulas.imageFViewPust.y + pipeUstYukseklik &&
            bSolUy >= ulas.imageFViewPust.y && bSolUy <= ulas.imageFViewPust.y + pipeUstYukseklik &&
            bSagUx >= ulas.imageFViewPust.x && bSagUx <= ulas.imageFViewPust.x + pipeUstGenislik &&
            bSolUx >= ulas.imageFViewPust.x && bSolUx <= ulas.imageFViewPust.x + pipeUstGenislik
            ||
            bSagUy >= ulas.imageFViewPust2.y && bSagUy <= ulas.imageFViewPust2.y + pipeUstYukseklik &&
            bSolUy >= ulas.imageFViewPust2.y && bSolUy <= ulas.imageFViewPust2.y + pipeUstYukseklik &&
            bSagUx >= ulas.imageFViewPust2.x && bSagUx <= ulas.imageFViewPust2.x + pipeUstGenislik &&
            bSolUx >= ulas.imageFViewPust2.x && bSolUx <= ulas.imageFViewPust2.x + pipeUstGenislik
            ||
            bSagAy >= ulas.imageFViewPalt.y && bSagAy <= ulas.imageFViewPalt.y + pipeUstYukseklik &&
            bSolAy >= ulas.imageFViewPalt.y && bSolAy <= ulas.imageFViewPalt.y + pipeUstYukseklik &&
            bSagAx >= ulas.imageFViewPalt.x && bSagAx <= ulas.imageFViewPalt.x + pipeUstGenislik &&
            bSolAx >= ulas.imageFViewPalt.x && bSolAx <= ulas.imageFViewPalt.x + pipeUstGenislik
            ||
            bSagAy >= ulas.imageFViewPalt2.y && bSagAy <= ulas.imageFViewPalt2.y + pipeUstYukseklik &&
            bSolAy >= ulas.imageFViewPalt2.y && bSolAy <= ulas.imageFViewPalt2.y + pipeUstYukseklik &&
            bSagAx >= ulas.imageFViewPalt2.x && bSagAx <= ulas.imageFViewPalt2.x + pipeUstGenislik &&
            bSolAx >= ulas.imageFViewPalt2.x && bSolAx <= ulas.imageFViewPalt2.x + pipeUstGenislik){

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