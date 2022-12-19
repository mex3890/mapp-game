package br.com.mappgame.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.LoginResponse
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textViewRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        buttonLogin.setOnClickListener {

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.loginUser(email, password)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        val code = response.code()

                        if (code == 202) {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.error,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        if (code == 200) {
                            SharedPrefManager.getInstance(applicationContext)
                                .saveUser(response.body()?.user!!)

                            if (response.body()?.user!!.role == 1) {
                                val intent =
                                    Intent(applicationContext, UserProfileActivity::class.java)

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)
                            } else {
                                val intent = Intent(applicationContext, ProfessionalActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)
                            }
                        }


                        if (response.code() == 500) {
                            Toast.makeText(
                                applicationContext,
                                "Internal Server error, try again latter!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        if (code == 203) {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Internal App Error, try again letter or verify your connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }

    override fun onStart() {
        super.onStart()

        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            if (SharedPrefManager.getInstance(this).user?.role == 1) {
                val intent = Intent(applicationContext, UserProfileActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else if (SharedPrefManager.getInstance(this).user?.role == 2) {
                val intent = Intent(applicationContext, ProfessionalActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}