package com.example.kumbarakala

import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStream

class CardActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        imageView = findViewById(R.id.cardImage)
        val btnShare = findViewById<Button>(R.id.btnShare)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // 🔹 Get Data
        val imageRes = intent.getIntExtra("image", 0)
        val text = intent.getStringExtra("text") ?: ""
        val artisan = intent.getStringExtra("artisan") ?: "Local Artisan"
        val phone = intent.getStringExtra("phone") ?: "N/A"

        // 🔹 Original Image
        val original = BitmapFactory.decodeResource(resources, imageRes)

        // 🔹 Create Bitmap
        bitmap = Bitmap.createBitmap(
            original.width,
            original.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(original, 0f, 0f, null)

        // 🔹 TEXT PAINT
        val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 70f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER
        paint.setShadowLayer(5f, 2f, 2f, Color.BLACK)

        // 🔹 BACKGROUND BOX
        val bgPaint = Paint()
        bgPaint.color = Color.parseColor("#CC000000")

        val rectTop = bitmap.height - 300f
        val rectBottom = bitmap.height.toFloat()

        canvas.drawRect(
            0f,
            rectTop,
            bitmap.width.toFloat(),
            rectBottom,
            bgPaint
        )

        // 🔹 CENTER POSITION
        val centerX = bitmap.width / 2f
        val boxCenterY = rectTop + (rectBottom - rectTop) / 2
        val lineSpacing = 80f

        // 🔹 DRAW TEXT (CENTERED PERFECTLY)
        // Product Name (bigger)
        paint.textSize = 80f
        canvas.drawText(text, centerX, boxCenterY - lineSpacing, paint)

        // Artisan + Phone (slightly smaller)
        paint.textSize = 60f
        canvas.drawText("By: $artisan", centerX, boxCenterY, paint)
        canvas.drawText("Ph: $phone", centerX, boxCenterY + lineSpacing, paint)

        // 🔹 SET IMAGE
        imageView.setImageBitmap(bitmap)

        // 🔹 SHARE BUTTON
        btnShare.setOnClickListener {
            val path = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "StoryCard",
                null
            )

            val uri = Uri.parse(path)

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        // 🔹 SAVE BUTTON
        btnSave.setOnClickListener {
            saveImageToGallery(bitmap)
        }
    }

    // 🔹 SAVE FUNCTION
    private fun saveImageToGallery(bitmap: Bitmap) {

        val filename = "StoryCard_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            fos = imageUri?.let { contentResolver.openOutputStream(it) }

        } else {

            val imagesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )

            val image = java.io.File(imagesDir, filename)
            fos = java.io.FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }
}