package br.com.mappgame.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_update_user.*
import kotlinx.android.synthetic.main.activity_user_set_license.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSetLicenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_set_license)

        val id = intent.getIntExtra("id", 0)

        userLicenseButtonReturn.setOnClickListener {
            intent = Intent(applicationContext, UpdateUserActivity::class.java)
            startActivity(intent)
        }

        userLicenseButtonProfile.setOnClickListener {
            intent = Intent(applicationContext, UpdateUserActivity::class.java)
            startActivity(intent)
        }

        userLicenseButtonProfiles.setOnClickListener {
            intent = Intent(applicationContext, UserProfileActivity::class.java)
            startActivity(intent)
        }

        userLicenseButtonLogout.setOnClickListener {
            SharedPrefManager.getInstance(applicationContext).clear()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = 0

            startActivity(intent)
        }

        buttonSendLicense.setOnClickListener {
            val license = editTextLicense.text.toString().trim()

            if (license.isEmpty()) {
                editTextName.error = "License required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.storeLicense(id, license)
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
                            SharedPrefManager.getInstance(applicationContext).clear()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.flags = 0
                            Toast.makeText(
                                applicationContext,
                                response.body()?.message, Toast.LENGTH_LONG
                            ).show()
                            startActivity(intent)
                        }
                    }
                })
        }
    }
}