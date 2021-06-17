package com.muhamapps.filmcatalogueapp1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.muhamapps.filmcatalogueapp1.databinding.ActivitySplashScreenBinding
import com.muhamapps.filmcatalogueapp1.home.HomeActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        setContentView(binding.root)

        val left = AnimationUtils.loadAnimation(this, R.anim.left_animation)
        val right = AnimationUtils.loadAnimation(this, R.anim.right_animation)

        binding.ivSplash.startAnimation(left)
        binding.tvSplash.startAnimation(right)

        Handler(mainLooper).postDelayed({
            startActivity(
                Intent(this,
                    HomeActivity::class.java)
            )
            finish()
        }, 3500)
    }
}