package com.study.app_retrofit

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.study.app_retrofit.model.Photo
import kotlinx.android.synthetic.main.recyclerview_row.view.*

class RecyclerViewAdapter: RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    var items: List<Photo> = ArrayList()

    fun setListData(items: List<Photo>) {
        this.items = items
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val id = view.tv_id
        val thumbnail = view.iv_thumbnail
        val title = view.tv_title

        fun bind(photo: Photo, context: Context) {
            id.text = photo.id.toString()
            title.text = photo.title
            try {
                Picasso.get().load(photo.thumbnailUrl).into(thumbnail)
            } catch (e: Exception){}

            thumbnail.setOnClickListener {
                val image = ImageView(context)

                Picasso.get().load(photo.url).into(image)

                AlertDialog.Builder(context)
                    .setTitle("Photo")
                    .setView(image)
                    .setPositiveButton("close", null)
                    .show()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position], holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}