package com.example.covidtracker_kotlin

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CountryCodePicker
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var countryCodePicker: CountryCodePicker
    lateinit var mtodaytotal: TextView
    lateinit var mtotal: TextView
    lateinit var mactive: TextView
    lateinit var mtodayactive: TextView
    lateinit var mrecovered: TextView
    lateinit var mtodayrecovered: TextView
    lateinit var mdeaths: TextView
    lateinit var mtodaydeaths: TextView
    lateinit var country: String
    lateinit var mfilter: TextView
    lateinit var spinner: Spinner
    var types = arrayOf("cases", "deaths", "recovered", "active")
    lateinit var modelClassList: List<ModelClass>
    lateinit var modelClassList2: List<ModelClass>
    lateinit var mpiechart: PieChart
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()

        countryCodePicker = findViewById(R.id.ccp)
        mtodayactive = findViewById(R.id.todayactive)
        mactive = findViewById(R.id.activecase)
        mdeaths = findViewById(R.id.totaldeaths)
        mtodaydeaths = findViewById(R.id.todaydeath)
        mrecovered = findViewById(R.id.recoveredcase)
        mtodayrecovered = findViewById(R.id.todayrecovered)
        mtotal = findViewById(R.id.totalcase)
        mtodaytotal = findViewById(R.id.todaytotal)
        mpiechart = findViewById(R.id.piechart)
        spinner = findViewById(R.id.spinner)
        mfilter = findViewById(R.id.filter)
        recyclerView = findViewById(R.id.recyclerView)


        modelClassList = java.util.ArrayList<ModelClass>()
        modelClassList2 = java.util.ArrayList<ModelClass>()



        spinner.onItemSelectedListener = this

        val arrayAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_dropdown_item, types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = arrayAdapter


        ApiUtilities.getApiInterface()!!.getCountryData().enqueue(object :
            Callback<List<ModelClass>> {
            override fun onResponse(
                call: Call<List<ModelClass>>,
                response: Response<List<ModelClass>>
            ) {
                if (response.isSuccessful){
                    modelClassList2
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })

        adapter = Adapter(applicationContext, modelClassList2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        countryCodePicker.setAutoDetectedCountry(true)
        country = countryCodePicker.selectedCountryName
        countryCodePicker.setOnCountryChangeListener {
            country = countryCodePicker.selectedCountryName
            fetchdata()
        }


    }

    private fun fetchdata() {
        ApiUtilities.getApiInterface()!!.getCountryData().enqueue(object :
            Callback<List<ModelClass>> {
            override fun onResponse(
                call: Call<List<ModelClass>>,
                response: Response<List<ModelClass>>
            ) {
               // modelClassList?.addAll(response.body())
                for (i in modelClassList.indices) {

                    if (modelClassList[i].country == country) {

                        mactive.text = NumberFormat.getInstance().format(modelClassList[i].active)
                        mtodaydeaths.text =
                            NumberFormat.getInstance().format(modelClassList[i].todayDeaths)
                        mtodayrecovered.text =
                            NumberFormat.getInstance().format(modelClassList[i].todayRecovered)
                        mtodaytotal.text =
                            NumberFormat.getInstance().format(modelClassList[i].todayCases)
                        mtotal.text = NumberFormat.getInstance().format(modelClassList[i].cases)
                        mdeaths.text = NumberFormat.getInstance().format(modelClassList[i].deaths)
                        mrecovered.text =
                            NumberFormat.getInstance().format(modelClassList[i].recovered)

                        val active: Int = Integer.parseInt(modelClassList[i].active)
                        val total: Int = Integer.parseInt(modelClassList[i].cases)
                        val recovered: Int = Integer.parseInt(modelClassList[i].recovered)
                        val deaths: Int = Integer.parseInt(modelClassList[i].deaths)


                        updateGraph(active, total, recovered, deaths)
                    }

                }
            }

            override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun updateGraph(active: Int, total: Int, recovered: Int, deaths: Int) {


        mpiechart.clearChart()
        mpiechart.addPieSlice(PieModel("Confirm", total.toFloat(), Color.parseColor("#ffb701")))
        mpiechart.addPieSlice(PieModel("Active", active.toFloat(), Color.parseColor("#ff4caf50")))
        mpiechart.addPieSlice(
            PieModel(
                "Recovered",
                recovered.toFloat(),
                Color.parseColor("#38ACCD")
            )
        )
        mpiechart.addPieSlice(PieModel("Deaths", deaths.toFloat(), Color.parseColor("#f55c47")))
        mpiechart.startAnimation()

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item: String = types[position]
        mfilter.text = item
        adapter.filter(item)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


}