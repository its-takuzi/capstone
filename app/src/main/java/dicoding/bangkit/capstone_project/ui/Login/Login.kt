package dicoding.bangkit.capstone_project.ui.Login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.sharedpreference.sharedpreferencetoken
import dicoding.bangkit.capstone_project.Api.Login
import dicoding.bangkit.capstone_project.databinding.ActivityLoginBinding
import dicoding.bangkit.capstone_project.ui.homepage.Homepage

class Login : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedpreferencetoken: sharedpreferencetoken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedpreferencetoken = sharedpreferencetoken(this)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setupView()
        binding.emailEditText.validateInput(0)
        binding.passwordEditText.validateInput(1)
        validatelogin()
        playAnimation()
    }

    private fun validatelogin(){
        binding.loginButton.setOnClickListener{
            val email = binding.emailEditText
            val password = binding.passwordEditText
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                Toast.makeText(this, "Pastikan anda telah mengisi input email dan password!", Toast.LENGTH_SHORT).show()
            }
            if (email.isEmailValid && password.isPasswordValid){
                viewModel.postlogin(Login(email.text.toString(), password.text.toString()))
                viewModel.loginResponse.observe(this){ response ->
                    showLoading(false)
                    response?.let {
                        if (it.userCredential != null) {
                            val user = it.userCredential.user
                            val token = it.userCredential.tokenResponse?.idToken
                            val providerData = user?.providerData?.firstOrNull()
                            val name = providerData?.displayName ?: ""

                            sharedpreferencetoken.saveToken(token ?: "", name, email.text.toString())
                            startActivity(Intent(this, Homepage::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed. Try Again!", Toast.LENGTH_SHORT).show()
                        }
                    } ?: run {
                        Toast.makeText(this, "Login Failed. Try Again!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Invalid email or password format!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun showLoading(state : Boolean){
        if(state){
            binding.pbProgressBar.visibility = View.VISIBLE
        }else{
            binding.pbProgressBar.visibility = View.GONE
        }
    }

}