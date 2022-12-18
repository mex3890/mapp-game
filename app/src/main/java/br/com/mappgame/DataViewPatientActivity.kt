package br.com.mappgame

import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidplot.util.PixelUtils
import com.androidplot.xy.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration



class DataViewPatientActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view_patient)

        // initialize our XYPlot reference:
        val plot = findViewById<XYPlot>(R.id.plot)

        val dados = buscaDados()

        val series1 = dados.acertos
        val series2 = dados.erros

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        val series1Format = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels)

        val series2Format = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels_2)

        // add an "dash" effect to the series2 line:
        series2Format.linePaint.pathEffect = DashPathEffect(
            floatArrayOf(
                // always use DP when specifying pixel sizes, to keep things consistent across devices:
                PixelUtils.dpToPix(20f),
                PixelUtils.dpToPix(15f)
            ), 0f
        )

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.interpolationParams = CatmullRomInterpolator.Params(
            10,
            CatmullRomInterpolator.Type.Centripetal
        )
        series2Format.interpolationParams = CatmullRomInterpolator.Params(
            10,
            CatmullRomInterpolator.Type.Centripetal
        )

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format)
        plot.addSeries(series2, series2Format)

    }
}

fun buscaDados(): Array<Acertos> {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://api.example.com/endpoint")
        .header("Content-Type", "application/json")
        .build()

    val response = client.newCall(request).execute()
    val responseBody = response.body!!.string()

    val json = Json(JsonConfiguration.Stable)
    val data = json.parse(Array<Acertos>::class.serializer(), jsonData)

    return data.toList()
}

@Serializable
data class Acertos(val acertos: Int, val erros: Int)