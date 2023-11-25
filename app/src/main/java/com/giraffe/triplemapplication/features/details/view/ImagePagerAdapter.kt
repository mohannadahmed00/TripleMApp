package com.giraffe.triplemapplication.features.details.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.giraffe.triplemapplication.model.products.Image
import com.giraffe.triplemapplication.utils.load

class ImagePagerAdapter(private val context: Context, private val images: List<Image>?) :
    PagerAdapter() {
    override fun getCount(): Int {
        return if (images?.size == null) {
            0
        } else {
            images.size
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.load(images?.get(position)?.src.toString())
        // Load image using Glide
//        Glide.with(context)
//            .load(images?.get(position)?.src)
//            .into(imageView)

        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

}
