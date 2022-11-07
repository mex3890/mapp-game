package br.com.mappgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginActBtn = findViewById<Button>(R.id.loginBtn)
        loginActBtn.setOnClickListener {
            val Intent = Intent(this, LoginActivity::class.java)
            startActivity(Intent)
        }

        val optionsActBtn = findViewById<Button>(R.id.optionsBtn)
        optionsActBtn.setOnClickListener{
            val Intent = Intent(this,OptionsActivity::class.java)
            startActivity(Intent)
        }
    }
}