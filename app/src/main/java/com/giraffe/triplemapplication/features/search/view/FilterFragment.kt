package com.giraffe.triplemapplication.features.search.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.forEach
import androidx.navigation.fragment.findNavController
import com.giraffe.triplemapplication.bases.BaseFragment
import com.giraffe.triplemapplication.databinding.FragmentFilterBinding
import com.giraffe.triplemapplication.features.details.view.ColorsAdapter
import com.giraffe.triplemapplication.features.details.view.OnColorClickListener
import com.giraffe.triplemapplication.features.search.viewmodel.FilterOptions
import com.giraffe.triplemapplication.features.search.viewmodel.SearchVM
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class FilterFragment : BaseFragment<SearchVM, FragmentFilterBinding>(), OnColorClickListener {
    private val selectedChips: MutableList<String> =
        mutableListOf() // List to store selected chip texts
    private var selectedColor: String = ""
    private var selectedCategory: String = ""
    private lateinit var colorsAdapter: ColorsAdapter
    private var max : Double? = null
    private var min : Double? = null
    override fun getViewModel(): Class<SearchVM> = SearchVM::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        b: Boolean,
    ): FragmentFilterBinding = FragmentFilterBinding.inflate(inflater, container, false)

    override fun handleView() {
        binding.menRadio.isChecked =false
        colorsAdapter = ColorsAdapter(this, FilterOptions.colorsList)
        binding.colorRv.adapter = colorsAdapter
    }

    override fun handleClicks() {
        binding.categoryRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            val checkedRadioButton: RadioButton? = binding.root.findViewById(checkedId)

            // Check if a RadioButton is checked
            checkedRadioButton?.let {
                val textOfCheckedRadioButton: String = it.text.toString()
                selectedCategory = textOfCheckedRadioButton.lowercase()

            }
        }

        // Set checked change listener for each chip
        binding.brandsChipgroup.forEach { view ->
            if (view is Chip) {
                view.setOnClickListener {
                    val chip = it as Chip
                    val chipText = chip.text.toString()

                    if (chip.isChecked) {
                        selectedChips.add(chipText.uppercase())


                    } else {
                        selectedChips.remove(chipText.uppercase())


                    }

                }
            }
        }
        binding.apply.setOnClickListener {

            if(!binding.minEditText.text.isNullOrEmpty() && !binding.maxEditText.text.isNullOrEmpty() ){
                max = binding.maxEditText.text.toString().toDouble()
                min = binding.minEditText.text.toString().toDouble()
            }

            val filtered = mViewModel.filterSearch(
                selectedChips,
                selectedColor,
                selectedCategory,
                min,
                max,
                products = sharedViewModel.allProducts.value!!
            )
            sharedViewModel.setFiltered(filtered)


            findNavController().navigateUp()
        }
    }



    override fun onColorClickListener(color: String) {
        selectedColor = color


    }


}