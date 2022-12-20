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
import kotlinx.android.synthetic.main.activity_professional_show_patient.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalShowPatient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_show_patient)

        val name = intent.getStringExtra("name")
        val id = intent.getIntExtra("id", 0)
        val user = SharedPrefManager.getInstance(applicationContext).user

        text_name_patient.text = name

        profileButtonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProfessionalShowPatient)
            builder.setMessage("Do you really want to unlink the profile?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, _ ->
                    if (user != null) {
                        RetrofitClient.instance.deletePatientProfessionalRelation(id, user.id)
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

                                    Toast.makeText(
                                        applicationContext,
                                        response.code().toString(),
                                        Toast.LENGTH_LONG
                                    ).show()

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
                                                this@ProfessionalShowPatient,
                                                ProfessionalActivity::class.java
                                            )
                                        )
                                    }
                                }
                            })
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }
}