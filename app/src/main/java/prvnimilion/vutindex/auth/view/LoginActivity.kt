package prvnimilion.vutindex.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel
import prvnimilion.vutindex.BaseActivity
import prvnimilion.vutindex.R
import prvnimilion.vutindex.auth.viewmodel.LoginViewModel
import prvnimilion.vutindex.databinding.LoginScreenBinding
import prvnimilion.vutindex.home.view.HomeActivity
import prvnimilion.vutindex.ui_common.dialogs.PrivacyPolicyPopup

class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var binding: LoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        fillCredentials()
        observeViewModel()
        observeButtons()
    }

    private fun observeViewModel() {
        loginViewModel.userLoggedIn.observe(this, Observer {
            toggleLoading(false)
            if (it) {
                startHomeActivity()
            } else {
                Snackbar.make(binding.appIcon, getString(R.string.login_wrong_password), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeButtons() {
        binding.loginButton.setOnClickListener {
            if (binding.usernameEt.text.toString().isNotEmpty() && binding.passwordEt.text.toString().isNotEmpty()) {
                loginViewModel.loginUser(binding.usernameEt.text.toString(), binding.passwordEt.text.toString())
                toggleLoading(true)
            }
        }
        binding.privacyPolicy.setOnClickListener {
            showPrivacyPolicy()
        }
    }

    private fun showPrivacyPolicy(){
        val privacyPolicyPopup = PrivacyPolicyPopup()
        privacyPolicyPopup.show(supportFragmentManager, PRIVACY_POLICY_POPUP_TAG)
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBackground.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.loadingBackground.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun fillCredentials() {
        loginViewModel.getCredentials()

        loginViewModel.userCredentials.observe(this, Observer {
            binding.usernameEt.setText(it.first)
            binding.passwordEt.setText(it.second)
        })
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


    companion object {
        const val PRIVACY_POLICY_POPUP_TAG = "privacy_policy"
    }

}