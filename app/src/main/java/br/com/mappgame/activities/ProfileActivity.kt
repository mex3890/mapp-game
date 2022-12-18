package br.com.mappgame.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import br.com.mappgame.R
import br.com.mappgame.models.Patient
import br.com.mappgame.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_update_user.*

class ProfileActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileId = intent.getIntExtra("id", 0)
        val profileName = intent.getStringExtra("name")
        val profileBirthDate = intent.getStringExtra("birthdate")
        val patientUserId = intent.getIntExtra("userId", 0)

        val textViewProfileWelcome = welcome_text_profile

        textViewProfileWelcome.text = "Welcome, ".plus(profileName)

        profileButtonReturn.setOnClickListener {
            val intent = Intent(applicationContext, UserProfileActivity::class.java)

            startActivity(intent)
        }

        profileButtonPlay.setOnClickListener {
            val intent = Intent(applicationContext, StageOne::class.java)
            intent.putExtra("patient_id", profileId)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if (!SharedPrefManager.getInstance(this).isLoggedIn) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}