package com.example.kumbarakala

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var fullList: ArrayList<Product>   // for search backup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val searchBar = findViewById<EditText>(R.id.searchBar)

        // 🔹 Product List
        productList = arrayListOf(

            Product("Clay Pot", R.drawable.pot,R.drawable.pot1,R.drawable.pot2,
                "Keeps water naturally cool",
                "Clay pots are eco-friendly and naturally cool water without electricity. They enhance taste and improve digestion. Ideal for daily use."),

            Product("Lamp", R.drawable.lamp,R.drawable.lamp1,R.drawable.lamp2,
                "Traditional lighting",
                "Clay lamps are widely used in festivals like Diwali. They are biodegradable and symbolize positivity and tradition."),

            Product("Clay Bottle", R.drawable.bottle,R.drawable.bottle1,R.drawable.bottle2,
                "Maintains temperature",
                "Clay bottles keep water naturally cool and fresh. They are free from harmful chemicals and suitable for daily hydration."),

            Product("Clay Cup", R.drawable.clay_cup,R.drawable.cup1,R.drawable.cup2,
                "Perfect for tea",
                "Clay cups enhance the flavor of beverages like tea and coffee. They are eco-friendly and disposable alternatives."),

            Product("Cooking Pot", R.drawable.cook_pot,R.drawable.cook1,R.drawable.cook2,
                "For traditional cooking",
                "Clay cooking pots distribute heat evenly and retain nutrients. Food cooked in clay tastes better and healthier."),

            Product("Flower Pot", R.drawable.flower_pot,R.drawable.flower1,R.drawable.flower2,
                "For plants",
                "Clay flower pots provide proper air circulation to roots, helping plants grow healthier and stronger."),

            Product("Storage Jar", R.drawable.storage_jar,R.drawable.jar1,R.drawable.jar2,
                "Store grains",
                "Clay jars keep food fresh by absorbing moisture naturally. Best for storing grains and pickles."),

            Product("Clay Plate", R.drawable.clay_plate,R.drawable.plate1,R.drawable.plate2,
                "Safe for serving",
                "Clay plates are natural and non-toxic. They enhance the traditional dining experience."),

            // ⭐ NEW PRODUCTS

            Product("Clay Tawa", R.drawable.tawa,R.drawable.tawa1,R.drawable.tawa2,
                "For cooking roti",
                "Clay tawa is used for making chapati and dosa. It provides even heating and enhances the natural taste of food."),

            Product("Clay Bowl", R.drawable.bowl,R.drawable.bowl1,R.drawable.bowl2,
                "Serving bowl",
                "Clay bowls are perfect for serving curries and snacks. They retain heat and add a traditional touch to meals."),

            Product("Clay Piggy Bank", R.drawable.piggy_bank,R.drawable.pig1,R.drawable.pig2,
                "Save money",
                "Traditional clay piggy banks are used for saving money. They promote the habit of saving among children."),

            Product("Clay Water Filter", R.drawable.water_filter,R.drawable.filter1,R.drawable.filter2,
                "Natural filtration",
                "Clay water filters purify water naturally without electricity. They are cost-effective and eco-friendly.")
        )

        // Backup list for search
        fullList = ArrayList(productList)

        // 🔹 RecyclerView Setup
        adapter = ProductAdapter(productList, this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        // 🔹 Search Feature
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
        })
    }

    // 🔍 Filter Function
    private fun filterList(query: String) {
        val filteredList = ArrayList<Product>()

        for (item in fullList) {
            if (item.name.lowercase().contains(query.lowercase())) {
                filteredList.add(item)
            }
        }

        adapter.setFilteredList(filteredList)
    }
}