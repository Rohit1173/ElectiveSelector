package com.example.electiveselector.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.electiveselector.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private var images: MutableList<Int>) :
    SliderViewAdapter<SliderAdapter.Holder>() {
    class Holder(itemView: View) : ViewHolder(itemView) {
        var slider_img: ImageView = itemView.findViewById(R.id.slider_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: Holder, position: Int) {
        viewHolder.slider_img.setImageResource(images[position])
    }

    override fun getCount(): Int {
        return images.size
    }
}