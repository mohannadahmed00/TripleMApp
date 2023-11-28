package com.giraffe.triplemapplication.features.search.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.forEach
import androidx.core.view.get
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentFilterBinding
import com.giraffe.triplemapplication.features.details.view.ColorsAdapter
import com.giraffe.triplemapplication.features.details.view.OnColorClickListener
import com.giraffe.triplemapplication.features.search.viewmodel.FilterOptions
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class FilterFragment : BaseFragment<SearchVM, FragmentFilterBinding>() ,OnColorClickListener {
    private val selectedChips: MutableList<String> =
        mutableListOf() // List to store selected chip texts
    private var selectedColor: String = ""
    private lateinit var colorsAdapter: ColorsAdapter
    override fun getViewModel(): Class<SearchVM> = SearchVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentFilterBinding = FragmentFilterBinding.inflate(inflater, container, false)

    override fun handleView() {
        colorsAdapter = ColorsAdapter(this , FilterOptions.colorsList)
        binding.colorRv.adapter = colorsAdapter
    }

    override fun handleClicks() {
        binding.categoryRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val checkedRadioButton: RadioButton? = binding.root.findViewById(checkedId)

            // Check if a RadioButton is checked
            checkedRadioButton?.let {
                val textOfCheckedRadioButton: String = it.text.toString()
                mViewModel.setCategory(textOfCheckedRadioButton)
            }
        }
        binding.saleSwitch.setOnCheckedChangeListener { compoundButton, b ->
            mViewModel.setOnSale(b)
        }
        // Set checked change listener for each chip
        binding.brandsChipgroup.forEach { view ->
            if (view is Chip) {
                view.setOnClickListener {
                    val chip = it as Chip
                    val chipText = chip.text.toString()

                    if (chip.isChecked) {
                        selectedChips.add(chipText.lowercase())


                    } else {
                        selectedChips.remove(chipText.lowercase())


                    }
                    mViewModel.setBrands(selectedChips)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onColorClickListener(color: String) {
        selectedColor =color
        mViewModel.setColor(selectedColor)
        showToast(selectedColor)
    }


}