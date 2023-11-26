package com.giraffe.triplemapplication.features.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.giraffe.triplemapplication.R
import java.util.Objects

class SliderAdapter(
    private val context: Context,
    private val codes: List<String>,
    private val onCodeClick: OnCodeClick
) : PagerAdapter() {
    override fun getCount(): Int {
        return codes.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as CardView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.item_slider, container, false)
        //val imageView: ImageView = itemView.findViewById<View>(R.id.slider_image) as ImageView
        val card: CardView = itemView.findViewById<CardView>(R.id.card) as CardView
        val tvCode: TextView = itemView.findViewById<View>(R.id.tv_code) as TextView
        //imageView.setImageResource(imageList[position])
        tvCode.text = codes[position]
        card.setOnLongClickListener {
            onCodeClick.onClick(codes[position])
            true
        }

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CardView)
    }
    interface OnCodeClick{
        fun onClick(code:String)
    }
}