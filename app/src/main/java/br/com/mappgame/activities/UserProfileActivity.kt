package br.com.mappgame.activities

import android.annotation.SuppressLint
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
import br.com.mappgame.models.UserPatientsIndexResponse
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_user_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val user = SharedPrefManager.getInstance(this).user
        if (SharedPrefManager.getInstance(this).isLoggedIn) {

            if (user != null) {
                RetrofitClient.instance.userPatients(user.id)
                    .enqueue(object : Callback<UserPatientsIndexResponse> {

                        override fun onFailure(
                            call: Call<UserPatientsIndexResponse>,
                            t: Throwable
                        ) {
                            Toast.makeText(
                                applicationContext,
                                "Internal App Error, try again letter or verify your connection",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        @SuppressLint("ResourceAsColor")
                        override fun onResponse(
                            call: Call<UserPatientsIndexResponse>,
                            response: Response<UserPatientsIndexResponse>
                        ) {
                            val code = response.code()

                            if (code == 200) {
                                val patientList = response.body()?.patients
                                val layout = profileList

                                patientList?.forEach { element ->
                                    val horizontalLayout = LinearLayout(applicationContext)
                                    val button = Button(applicationContext)
                                    val label = TextView(applicationContext)

                                    horizontalLayout.layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                    )

                                    val view = findViewById<RelativeLayout>(R.id.user_profiles_view)

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
                                    button.text = "> >"
                                    button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50F)
                                    button.setTextColor(Color.parseColor("#2196F3"))
                                    button.setTypeface(null, Typeface.BOLD)

                                    button.setOnClickListener {
                                        val intent =
                                            Intent(applicationContext, ProfileActivity::class.java)
                                        intent.putExtra("name", element.name)
                                        intent.putExtra("id", element.id)
                                        intent.putExtra("birthDate", element.birth_date)
                                        intent.putExtra("userId", element.user_id)
                                        startActivity(intent)
                                    }

                                    horizontalLayout.addView(label)
                                    horizontalLayout.addView(button)
                                    layout.addView(horizontalLayout)
                                }
                            }


                            if (response.code() == 500) {
                                Toast.makeText(
                                    applicationContext,
                                    "Internal Server error, try again latter!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    })
            }
        }

        userButtonReturn.setOnClickListener {
            val builder = AlertDialog.Builder(this@UserProfileActivity)
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

        userButtonLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@UserProfileActivity)
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

        userButtonProfile.setOnClickListener {
            val intent = Intent(applicationContext, UpdateUserActivity::class.java)

            startActivity(intent)
        }

        newProfileButton.setOnClickListener {
            val intent = Intent(applicationContext, CreateNewProfile::class.java)
            if (user != null) {
                intent.putExtra("userId", user.id)
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
