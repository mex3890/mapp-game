package br.com.mappgame.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.mappgame.R
import br.com.mappgame.api.RetrofitClient
import br.com.mappgame.models.PatientAnswersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate


class DataViewPatientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view_patient)

        // Obter uma referência para o gráfico de barras
        val chart: BarChart = findViewById(R.id.chart)

        RetrofitClient.instance.patientAnswers(1)
            .enqueue(object : Callback<PatientAnswersResponse> {

                override fun onFailure(call: Call<PatientAnswersResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Internal App Error, try again letter or verify your connection",
                        Toast.LENGTH_LONG
                    ).show()
                }

                @SuppressLint("ResourceAsColor")
                override fun onResponse(
                    call: Call<PatientAnswersResponse>,
                    response: Response<PatientAnswersResponse>
                ) {
                    val code = response.code()

                    if (code == 200) {

                        val answers =  response.body()?.answers
                        val hits = answers?.map { it.hits }
                        val errors = answers?.map { it.errors }
                        val date = answers?.map { it.created_at }

                        Toast.makeText(applicationContext,
                            hits.toString(), Toast.LENGTH_LONG).show()
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

        // Criar uma lista de entradas a partir do array de valores
        val entries = ArrayList<BarEntry>()
        for (i in hits.indices) {
            entries.add(BarEntry(i.toFloat(), hits[i]))
        }

        // Criar um conjunto de dados a partir da lista de entradas
        val dataSet = BarDataSet(entries, "Label")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS

        // Criar um objeto de dados a partir do conjunto de dados
        val data = BarData(dataSet)

        // Atribuir os dados ao gráfico de barras
        chart.data = data

        // Atualizar o gráfico
        chart.invalidate()
    }
}