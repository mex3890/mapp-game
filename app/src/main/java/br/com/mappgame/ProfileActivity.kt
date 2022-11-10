package br.com.mappgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val newProfileActBtn = findViewById<Button>(R.id.buttonAddProfile)
        newProfileActBtn.setOnClickListener{
            val Intent = Intent(this, NewProfileActivity::class.java)
            startActivity(Intent)
        }
    }
}