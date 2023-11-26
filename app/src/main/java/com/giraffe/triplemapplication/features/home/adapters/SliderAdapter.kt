package com.giraffe.triplemapplication.features.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.model.discount.PriceRule
import java.util.Objects

class SliderAdapter(
    private val context: Context,
    private val codes: List<PriceRule>,
    private val onCodeClick: OnCodeClick
) : PagerAdapter() {
    override fun getCount(): Int {
        return codes.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.item_slider, container, false)
        //val imageView: ImageView = itemView.findViewById<View>(R.id.slider_image) as ImageView
        val card: ConstraintLayout = itemView.findViewById(R.id.root) as ConstraintLayout
        val tvCode: TextView = itemView.findViewById<View>(R.id.tv_code) as TextView
        val tvGift: TextView = itemView.findViewById<View>(R.id.tv_gift) as TextView
        val tvHint: TextView = itemView.findViewById<View>(R.id.tv_hint) as TextView
        //imageView.setImageResource(imageList[position])
        tvCode.text = codes[position].title.subSequence(0,codes[position].title.length/2).toString().plus("****")
        tvHint.text = context.getString(
            R.string.for_orders_that_is_greater_than_or_equal,
            codes[position].prerequisite_subtotal_range.greater_than_or_equal_to.toDouble()
        )
        if (codes[position].value_type=="percentage"){
            tvGift.text = codes[position].value.plus("%")
        }else{
            tvGift.text = codes[position].value
        }

        card.setOnLongClickListener {
            onCodeClick.onClick(codes[position].title)
            true
        }

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
    interface OnCodeClick{
        fun onClick(code:String)
    }
}