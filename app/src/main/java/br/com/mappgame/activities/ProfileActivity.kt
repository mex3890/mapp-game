package br.com.mappgame.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_update_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileId = intent.getIntExtra("id", 0)
        val profileName = intent.getStringExtra("name")
        val profileBirthDate = intent.getStringExtra("birthDate")
        val patientUserId = intent.getIntExtra("userId", 0)

        val textViewProfileWelcome = welcome_text_profile

        textViewProfileWelcome.text = "Welcome, ".plus(profileName)

        profileButtonProfiles.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)

            startActivity(intent)
        }

        profileButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)

            startActivity(intent)
        }

        profileButtonPlay.setOnClickListener {
            val intent = Intent(applicationContext, StageOne::class.java)
            intent.putExtra("patient_id", profileId)
            intent.putExtra("profileName", profileName)
            intent.putExtra("profileBirthDate", profileBirthDate.toString())
            intent.putExtra("patientUserID", patientUserId)
            startActivity(intent)
        }

        profileButtonProfile.setOnClickListener {
            val intent = Intent(applicationContext, UpdateProfileActivity::class.java)
            intent.putExtra("profileId", profileId)
            intent.putExtra("profileName", profileName)
            intent.putExtra("profileBirthDate", profileBirthDate.toString())
            intent.putExtra("patientUserId", patientUserId)
            startActivity(intent)
        }

        profileButtonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProfileActivity)
            builder.setMessage("Do you really want to delete the profile?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    RetrofitClient.instance.deletePatient(profileId)
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
                                            this@ProfileActivity,
                                            UserProfileActivity::class.java
                                        )
                                    )
                                }
                            }
                        })
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }

    override fun onStart() {
        super.onStart()

        if (!SharedPrefManager.getInstance(this).isLoggedIn) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}