package br.com.mappgame.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import kotlinx.android.synthetic.main.activity_create_new_profile.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        buttonSendRequest.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val emailConfirmation = editTextConfirmEmail.text.toString().trim()

            if (email.isEmpty()) {
                editTextEmail.error = "E-mail required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (emailConfirmation.isEmpty()) {
                editTextConfirmEmail.error = "E-mail confirmation required"
                editTextConfirmEmail.requestFocus()
                return@setOnClickListener
            }

            if (email != emailConfirmation) {
                Toast.makeText(
                    applicationContext,
                    "Email and email confirmation don't match",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            RetrofitClient.instance.forgotPassword(email)
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Internal App Error, try again letter or verify your connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.code() == 202) {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.error,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        if (response.code() == 500) {
                            Toast.makeText(
                                applicationContext,
                                "Internal Server error, try again latter!",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        if (response.code() == 200) {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.message,
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                })
        }
    }
}