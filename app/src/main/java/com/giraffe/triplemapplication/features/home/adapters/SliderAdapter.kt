package com.giraffe.triplemapplication.features.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.giraffe.triplemapplication.R
import java.util.*

class SliderAdapter(
    private val context: Context,
    private val imageList: List<Int>
) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as CardView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.item_slider, container, false)
        val imageView: ImageView = itemView.findViewById<View>(R.id.slider_image) as ImageView
        imageView.setImageResource(imageList[position])
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CardView)
    }
}