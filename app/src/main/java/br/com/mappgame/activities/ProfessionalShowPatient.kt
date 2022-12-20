package br.com.mappgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.mappgame.R
import kotlinx.android.synthetic.main.activity_professional_show_patient.*

class ProfessionalShowPatient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_show_patient)

        val name = intent.getStringExtra("name")
        val id = intent.getIntExtra("id", 0)
    }
}