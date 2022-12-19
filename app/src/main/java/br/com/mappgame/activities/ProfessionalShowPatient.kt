package br.com.mappgame.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.mappgame.R

class ProfessionalShowPatient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_show_patient)

        val name = intent.getStringExtra("name")
    }
}