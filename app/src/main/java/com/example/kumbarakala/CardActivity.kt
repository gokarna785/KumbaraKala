package com.example.kumbarakala

import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class CardActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        imageView = findViewById(R.id.cardImage)
        val btnShare = findViewById<Button>(R.id.btnShare)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Get data
        val imageRes = intent.getIntExtra("image", 0)
        val text = intent.getStringExtra("text") ?: ""
        val artisan = intent.getStringExtra("artisan") ?: "By Local Artisan"

        // Load image
        val original = BitmapFactory.decodeResource(resources, imageRes)
        bitmap = original.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.isAntiAlias = true

        // Background strip
        paint.color = Color.parseColor("#80000000")
        canvas.drawRect(
            0f,
            bitmap.height - 200f,
            bitmap.width.toFloat(),
            bitmap.height.toFloat(),
            paint
        )

        // Text
        paint.color = Color.WHITE
        paint.textSize = 40f
        canvas.drawText(text, 30f, bitmap.height - 120f, paint)

        paint.textSize = 30f
        canvas.drawText("KumbaraKala", 30f, bitmap.height - 70f, paint)
        canvas.drawText(artisan, 30f, bitmap.height - 30f, paint)

        imageView.setImageBitmap(bitmap)

        // Share button
        btnShare.setOnClickListener {
            shareImage()
        }

        // Save button
        btnSave.setOnClickListener {
            saveImageToGallery()
        }
    }

    // 🔗 SHARE FUNCTION
    private fun shareImage() {
        Thread {
            try {
                val file = File(cacheDir, "card.png")
                val output = FileOutputStream(file)

                bitmap.compress(Bitmap.CompressFormat.PNG, 90, output)
                output.flush()
                output.close()

                val uri: Uri = FileProvider.getUriForFile(
                    this,
                    "$packageName.provider",
                    file
                )

                runOnUiThread {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "image/*"
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    startActivity(Intent.createChooser(intent, "Share via"))
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    // 💾 SAVE TO GALLERY FUNCTION
    private fun saveImageToGallery() {
        try {
            val filename = "KumbaraKala_${System.currentTimeMillis()}.png"

            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/KumbaraKala")
            }

            val imageUri = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            val fos = resolver.openOutputStream(imageUri!!)

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            Toast.makeText(this, "Saved to Gallery ✅", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Save Failed ❌", Toast.LENGTH_SHORT).show()
        }
    }
}