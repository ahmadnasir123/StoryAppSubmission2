package com.sirdev.storyappsubmissionakhir.ui.register


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.sirdev.storyappsubmissionakhir.databinding.ActivityRegisterBinding
import com.sirdev.storyappsubmissionakhir.ui.login.LoginActivity
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userRegisterViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRegisterViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.registerBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString().trim()
            val emailUser = binding.emailEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()

            if (username.isNotEmpty() && emailUser.isNotEmpty() && password.isNotEmpty()) {
                if (password.length >= 8) {
                    userRegisterViewModel.userRegister(
                        username, emailUser, password
                    )
                } else {
                    formMessageToast("Isi form terlebih dahulu!")
                }
            }

            userRegisterViewModel.message.observe(this) { message ->
                formMessageToast(message)
                if (message == "User created") {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }

            userRegisterViewModel.isLoading.observe(this) { isLoading ->
                showingLoading(isLoading)
            }
        }

        supportActionBar?.hide()
    }

    private fun showingLoading(loading: Boolean) {
        binding.loadingBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun formMessageToast(messageForm: String) {
        if (messageForm == "Bad request") {
            val messageChange = "Email ini sudah pernah dibuat!"
            Toast.makeText(this, messageChange, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, messageForm, Toast.LENGTH_SHORT).show()
        }
    }
}