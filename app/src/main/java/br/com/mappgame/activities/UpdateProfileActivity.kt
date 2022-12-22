package br.com.mappgame.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import kotlinx.android.synthetic.main.activity_update_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        val id = intent.getIntExtra("profileId", 0)
        val name = intent.getStringExtra("profileName")
        val birthDate = intent.getStringExtra("profileBirthDate")
        val userId = intent.getIntExtra("patientUserId", 0)

        editBirthDate.setText(birthDate)
        editTextName.setText(name)

        profileUpdateButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("birthDate", birthDate)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        profileUpdateButtonProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("birthDate", birthDate)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        profileUpdateButtonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@UpdateProfileActivity)
            builder.setMessage("Do you really want to delete the profile?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    RetrofitClient.instance.deletePatient(id)
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
                                    startActivity(
                                        Intent(
                                            this@UpdateProfileActivity,
                                            UserProfileActivity::class.java
                                        )
                                    )
                                }
                            }
                        })
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        updateButtonProfile.setOnClickListener {
            val birthDateInput = editBirthDate.text.toString().trim()
            val nameInput = editTextName.text.toString().trim()

            if (nameInput.isEmpty()) {
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if (birthDateInput.isEmpty()) {
                editBirthDate.error = "Name required"
                editBirthDate.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.updatePatient(id, nameInput, birthDateInput)
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
                            val intent = Intent(applicationContext, ProfileActivity::class.java)
                            intent.putExtra("name", nameInput)
                            intent.putExtra("id", id)
                            intent.putExtra("birthDate", birthDateInput)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                        }
                    }
                })
        }

        profileUpdateButtonProfiles.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }
}