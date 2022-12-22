package br.com.mappgame.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.models.ProfessionalShowResponse
import br.com.mappgame.models.User
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_professional_update.*
import kotlinx.android.synthetic.main.activity_professional_update.buttonUpdate
import kotlinx.android.synthetic.main.activity_professional_update.editTextEmail
import kotlinx.android.synthetic.main.activity_professional_update.editTextName
import kotlinx.android.synthetic.main.activity_professional_update.editTextPassword
import kotlinx.android.synthetic.main.activity_professional_update.editTextPasswordConfirmation
import kotlinx.android.synthetic.main.activity_professional_update.editTextPhone
import kotlinx.android.synthetic.main.activity_update_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalUpdate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_update)

        val id = intent.getIntExtra("id", -1)

        RetrofitClient.instance.getProfessional(id)
            .enqueue(object : Callback<ProfessionalShowResponse> {
                override fun onFailure(call: Call<ProfessionalShowResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Internal App Error, try again letter or verify your connection",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<ProfessionalShowResponse>,
                    response: Response<ProfessionalShowResponse>
                ) {
                    if (response.code() == 500) {
                        Toast.makeText(
                            applicationContext,
                            "Internal Server error, try again latter!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    if (response.code() == 200) {
                        val professional = response.body()?.professional

                        if (professional != null) {
                            editTextName.setText(professional.name)
                            editTextEmail.setText(professional.email)
                            editTextPhone.setText(professional.phone)
                            editTextLicense.setText(professional.license)
                        }
                    }
                }
            })

        buttonUpdate.setOnClickListener {
            val user = SharedPrefManager.getInstance(applicationContext).user
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val passwordConfirmation = editTextPasswordConfirmation.text.toString().trim()
            var password = editTextPassword.text.toString().trim()
            val license = editTextLicense.text.toString().trim()


            if (name.isEmpty()) {
                editTextName.error = "Name required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if (phone.isEmpty()) {
                editTextPhone.error = "Phone required"
                editTextPhone.requestFocus()
                return@setOnClickListener
            }

            if (passwordConfirmation.isEmpty()) {
                editTextPasswordConfirmation.error = "Password Confirmation required"
                editTextPasswordConfirmation.requestFocus()
                return@setOnClickListener
            }

            if (license.isEmpty()) {
                editTextLicense.error = "License required"
                editTextLicense.requestFocus()
                return@setOnClickListener
            }

            if (user != null) {
                if (password.isEmpty()) {
                    password = "PasswordDefaultUnset2022@#*$"
                }

                RetrofitClient.instance.storeProfessional(
                    user.id,
                    name,
                    email,
                    phone,
                    license,
                    passwordConfirmation,
                    password
                ).enqueue(object : Callback<DefaultResponse> {
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
                                val intent =
                                    Intent(applicationContext, ProfessionalActivity::class.java)
                                SharedPrefManager.getInstance(applicationContext)
                                    .saveUser(User(user.id, email, name, phone, 1))
                                startActivity(intent)
                            }

                            if (response.code() == 201) {
                                SharedPrefManager.getInstance(applicationContext).clear()
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.flags = 0
                                startActivity(intent)
                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
            }
        }

        professionalUpdateButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, ProfessionalActivity::class.java)
            startActivity(intent)
        }

        professionalUpdateButtonLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProfessionalUpdate)
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

        professionalUpdateButtonProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfessionalActivity::class.java)
            startActivity(intent)
        }

        professionalUpdateButtonProfiles.setOnClickListener {
            val intent = Intent(applicationContext, ProfessionalActivity::class.java)
            startActivity(intent)
        }
    }
}