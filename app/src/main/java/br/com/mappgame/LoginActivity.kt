package br.com.mappgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val formLoginActBtn = findViewById<Button>(R.id.formLoginBtn)
        formLoginActBtn.setOnClickListener {
            val Intent = Intent(this, ProfileActivity::class.java)
            startActivity(Intent)
        }

        val registerActBtn = findViewById<Button>(R.id.registerBtn)
        registerActBtn.setOnClickListener {
            val Intent = Intent(this, RegisterActivity::class.java)
            startActivity(Intent)
        }
    }
}