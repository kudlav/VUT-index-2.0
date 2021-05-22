package cz.kudlav.vutindex.splash.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import cz.kudlav.vutindex.BaseActivity
import cz.kudlav.vutindex.R
import cz.kudlav.vutindex.auth.view.LoginActivity
import cz.kudlav.vutindex.home.view.HomeActivity
import cz.kudlav.vutindex.splash.viewmodel.SplashViewModel
import java.util.Timer
import kotlin.concurrent.schedule

class SplashActivity : BaseActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
    }

    override fun onResume() {
        super.onResume()

        splashViewModel.tryQuickLogin()
        observeViewModels()
    }

    private fun observeViewModels() {
        splashViewModel.userLoggedIn.observe(this, Observer {
            if (it) {
                showHomeActivity()
            } else {
                showLoginAfterDelay()
            }
        })
    }

    private fun showLoginAfterDelay() {
        Timer("Splash", false).schedule(1000) {
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }
}