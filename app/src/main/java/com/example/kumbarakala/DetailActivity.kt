package com.example.kumbarakala

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 🔹 Get Data
        val name = intent.getStringExtra("name") ?: ""
        val imageList = intent.getIntegerArrayListExtra("images") ?: arrayListOf()
        val shortDesc = intent.getStringExtra("description") ?: ""
        val fullDesc = intent.getStringExtra("fullDesc") ?: ""

        // 🔹 Views
        val nameText = findViewById<TextView>(R.id.productName)
        val shortText = findViewById<TextView>(R.id.productDescription)
        val fullText = findViewById<TextView>(R.id.fullDescription)

        val viewPager = findViewById<ViewPager2>(R.id.imageSlider)
        val dotsLayout = findViewById<LinearLayout>(R.id.dotsLayout)

        val editName = findViewById<EditText>(R.id.editName)
        val editPhone = findViewById<EditText>(R.id.editPhone)

        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val btnArtisan = findViewById<Button>(R.id.btnArtisan)

        // 🔹 Set Data
        nameText.text = name
        shortText.text = shortDesc
        fullText.text = fullDesc

        // ⭐ IMAGE SLIDER
        val adapter = ImageSliderAdapter(imageList)
        viewPager.adapter = adapter

        // ⭐ DOTS INDICATOR
        val dots = Array(imageList.size) { ImageView(this) }

        for (i in dots.indices) {
            dots[i] = ImageView(this)
            dots[i].setImageResource(android.R.drawable.presence_invisible)

            val params = LinearLayout.LayoutParams(30, 30)
            params.setMargins(6, 0, 6, 0)

            dotsLayout.addView(dots[i], params)
        }

        // Default dot
        if (dots.isNotEmpty()) {
            dots[0].setImageResource(android.R.drawable.presence_online)
        }

        // Change dot on swipe
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                for (i in dots.indices) {
                    dots[i].setImageResource(android.R.drawable.presence_invisible)
                }

                dots[position].setImageResource(android.R.drawable.presence_online)
            }
        })

        // ⭐ OPEN ARTISAN PAGE
        btnArtisan.setOnClickListener {
            startActivity(Intent(this, ArtisanActivity::class.java))
        }

        // ⭐ GENERATE STORY CARD
        btnGenerate.setOnClickListener {

            val currentPosition = viewPager.currentItem
            val selectedImage =
                if (imageList.isNotEmpty()) imageList[currentPosition] else 0

            val artisanName = editName.text.toString()
            val artisanPhone = editPhone.text.toString()

            val intent = Intent(this, CardActivity::class.java)

            intent.putExtra("image", selectedImage)
            intent.putExtra("text", name)
            intent.putExtra("artisan", artisanName)
            intent.putExtra("phone", artisanPhone)

            startActivity(intent)
        }
    }
}