package br.com.mappgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.storage.SharedPrefManager
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

        profileCreateButtonProfiles.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)
            startActivity(intent)
        }

        profileCreateButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)
            startActivity(intent)
        }

        profileCreateButtonProfile.setOnClickListener {
            val intent = Intent(applicationContext, UpdateUserActivity::class.java)
            startActivity(intent)
        }

        profileCreateButtonLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@CreateNewProfile)
            builder.setMessage("Do you really want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    SharedPrefManager.getInstance(this).clear()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = 0
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
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