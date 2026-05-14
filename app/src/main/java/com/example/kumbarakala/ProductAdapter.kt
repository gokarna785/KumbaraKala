package com.example.kumbarakala

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    var productList: ArrayList<Product>,
    val context: Context
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.productName)
        val image: ImageView = view.findViewById(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.name.text = product.name
        holder.image.setImageResource(product.image)

        holder.itemView.setOnClickListener {

            val intent = Intent(context, DetailActivity::class.java)

            intent.putExtra("name", product.name)
            // Pass all three images as an ArrayList
            val imageList = arrayListOf(product.image, product.image1, product.image2)
            intent.putIntegerArrayListExtra("images", imageList)
            intent.putExtra("description", product.shortDesc)
            intent.putExtra("fullDesc", product.fullDesc)

            context.startActivity(intent)
        }
    }

    // ⭐ Search Feature Fix
    fun setFilteredList(list: ArrayList<Product>) {
        this.productList = list
        notifyDataSetChanged()
    }
}