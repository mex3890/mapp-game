package br.com.mappgame.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.models.User
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_update_user.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        val user = SharedPrefManager.getInstance(this).user

        if (user != null) {
            editTextName.setText(user.name)
            editTextEmail.setText(user.email)
            editTextPhone.setText(user.phone)
        }

        buttonUpdate.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val passwordConfirmation = editTextPasswordConfirmation.text.toString().trim()
            var password = editTextPassword.text.toString().trim()
            val name = editTextName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()


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

            if (user != null) {
                if (password.isEmpty()) {
                    password = "PasswordDefaultUnset2022@#*$"
                }

                RetrofitClient.instance.updateUser(user.id, email, password, name, phone, passwordConfirmation)
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
                                val intent = Intent(applicationContext, UserProfileActivity::class.java)
                                SharedPrefManager.getInstance(applicationContext).saveUser(User(user.id, email, name, phone, 1))
                                startActivity(intent)
                            }

                            if (response.code() == 201) {
                                SharedPrefManager.getInstance(applicationContext).clear()
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.flags = 0
                                startActivity(intent)
                                Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            }
        }

        userUpdateButtonProfile.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)

            startActivity(intent)
        }

        userUpdateButtonProfiles.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)

            startActivity(intent)
        }

        userUpdateButtonLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@UpdateUserActivity)
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

        userUpdateButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)

            startActivity(intent)
        }

        buttonSetLicense.setOnClickListener {
            val intent = Intent(applicationContext, UserSetLicenseActivity::class.java)
            if (user != null) {
                intent.putExtra("id", user.id)
            }
            startActivity(intent)
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