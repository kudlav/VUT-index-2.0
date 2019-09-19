package prvnimilion.vutindex.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_screen.*
import org.koin.android.viewmodel.ext.android.viewModel
import prvnimilion.vutindex.BaseActivity
import prvnimilion.vutindex.R
import prvnimilion.vutindex.auth.viewmodel.LoginViewModel
import prvnimilion.vutindex.home.view.HomeActivity
import timber.log.Timber

class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
    }

    override fun onResume() {
        super.onResume()

        fillCredentials()
        observeViewModel()
        observeLoginButton()
    }

    private fun observeViewModel() {
        loginViewModel.userLoggedIn.observe(this, Observer {
            Snackbar.make(app_icon, "User logged in: $it", Snackbar.LENGTH_LONG).show()
            toggleLoading(false)
            if (it) {
                startHomeActivity()
            }
        })
    }

    private fun observeLoginButton() {
        login_button.setOnClickListener {
            if (username_et.text.toString().isNotEmpty() && password_et.text.toString().isNotEmpty()) {
                loginViewModel.loginUser(
                    username_et.text.toString(),
                    password_et.text.toString(),
                    remember_password_cb.isChecked
                )
                toggleLoading(true)
            }
        }
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) {
            loading_background.visibility = View.VISIBLE
            progress_bar.visibility = View.VISIBLE
        } else {
            loading_background.visibility = View.GONE
            progress_bar.visibility = View.GONE
        }
    }

    private fun fillCredentials() {
        loginViewModel.getCredentials()

        loginViewModel.userCredentials.observe(this, Observer {
            it.forEach { credentials ->
                if (credentials.key.isNotEmpty()) {
                    username_et.setText(credentials.key)
                    password_et.setText(credentials.value)
                    remember_password_cb.isChecked = true
                }
                return@Observer
            }
        })
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}