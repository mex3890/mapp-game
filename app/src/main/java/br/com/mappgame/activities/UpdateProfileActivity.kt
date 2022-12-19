package br.com.mappgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.models.User
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.editTextName
import kotlinx.android.synthetic.main.activity_update_user.*
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

        updateButtonProfile.setOnClickListener {
            val birthDate = editBirthDate.text.toString().trim()
            val name = editTextName.text.toString().trim()

            if (name.isEmpty()) {
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if (birthDate.isEmpty()) {
                editBirthDate.error = "Name required"
                editBirthDate.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.updatePatient(id, name, birthDate)
                .enqueue(object: Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, "Internal App Error, try again letter or verify your connection", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if(response.code() == 202) {
                            Toast.makeText(applicationContext, response.body()?.error, Toast.LENGTH_LONG).show()
                        }

                        if (response.code() == 500) {
                            Toast.makeText(applicationContext, "Internal Server error, try again latter!", Toast.LENGTH_LONG).show()
                        }


                        if (response.code() == 200) {
                            val intent = Intent(applicationContext, ProfileActivity::class.java)
                            intent.putExtra("name", name)
                            intent.putExtra("id", id)
                            intent.putExtra("birthDate", birthDate)
                            intent.putExtra("userId", userId)
                            startActivity(intent)
                        }
                    }
                })
        }

    }
}