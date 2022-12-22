package br.com.mappgame.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.DefaultResponse
import br.com.mappgame.models.ProfessionalSearchPatientsByEmailResponse
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_professional_add_patient.*
import kotlinx.android.synthetic.main.activity_update_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfessionalAddPatientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_add_patient)

        buttonSearch.setOnClickListener {
            val userEmail = editTextUserEmail.text.toString().trim()
            val user = SharedPrefManager.getInstance(applicationContext).user

            if (userEmail.isEmpty()) {
                editTextName.error = "License required"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.searchPatientsByUserEmail(userEmail)
                .enqueue(object : Callback<ProfessionalSearchPatientsByEmailResponse> {
                    override fun onFailure(
                        call: Call<ProfessionalSearchPatientsByEmailResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            applicationContext,
                            "Internal App Error, try again letter or verify your connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(
                        call: Call<ProfessionalSearchPatientsByEmailResponse>,
                        response: Response<ProfessionalSearchPatientsByEmailResponse>
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
                            val patientList = response.body()?.patients
                            val layout = viewPatientList

                            layout.removeAllViews()

                            patientList?.forEach { element ->

                                val horizontalLayout = LinearLayout(applicationContext)
                                val button = Button(applicationContext)
                                val label = TextView(applicationContext)

                                horizontalLayout.layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )

                                val view = findViewById<RelativeLayout>(R.id.professional_search)

                                label.text = element.name
                                label.setTextColor(Color.parseColor("#FFFFFF"))
                                label.minWidth = view.measuredWidth - 350
                                label.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
                                label.maxLines = 1

                                button.layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )

                                button.id = element.id
                                button.text = "+"
                                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
                                button.setTextColor(Color.parseColor("#2196F3"))
                                button.setTypeface(null, Typeface.BOLD)

                                button.setOnClickListener {
                                    horizontalLayout.removeAllViews()
                                    if (user != null) {
                                        addingPatientToProfessional(element.id, user.id)
                                    }
                                }

                                horizontalLayout.addView(label)
                                horizontalLayout.addView(button)
                                layout.addView(horizontalLayout)
                            }

                        }
                    }
                })
        }

        professionalButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, ProfessionalActivity::class.java)
            startActivity(intent)
        }

        professionalButtonPatients.setOnClickListener {
            val intent = Intent(applicationContext, ProfessionalActivity::class.java)
            startActivity(intent)
        }

        professionalButtonProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfessionalUpdate::class.java)
            SharedPrefManager.getInstance(applicationContext).user?.let { it1 -> intent.putExtra("id", it1.id) }
            startActivity(intent)
        }

        professionalButtonLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@ProfessionalAddPatientActivity)
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
    }

    fun addingPatientToProfessional(patientId: Int, userId: Int)
    {
        RetrofitClient.instance.storePatientFromProfessional(patientId, userId)
            .enqueue(object: Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Internal App Error, try again letter or verify your connection", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.code() == 500) {
                        Toast.makeText(applicationContext, "Internal Server error, try again latter!", Toast.LENGTH_LONG).show()
                    }

                    if (response.code() == 200) {
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                    }

                    if (response.code() == 202) {
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}