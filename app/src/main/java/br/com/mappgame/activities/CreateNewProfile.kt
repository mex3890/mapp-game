package br.com.mappgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import kotlinx.android.synthetic.main.activity_create_new_profile.*
import kotlinx.android.synthetic.main.activity_create_new_profile.editTextName
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_profile)

        val userId = intent.getIntExtra("userId", 1)

        profileCreateButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)
            startActivity(intent)
        }

        buttonCreate.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val birthDate = editBirthDate.text.toString().trim()

            if (name.isEmpty()) {
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if (birthDate.isEmpty()) {
                editBirthDate.error = "Birth date required"
                editBirthDate.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.createPatient(userId, birthDate, name)
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
                            startActivity(Intent(this@CreateNewProfile, UserProfileActivity::class.java))
                        }
                    }
                })
        }
    }
}