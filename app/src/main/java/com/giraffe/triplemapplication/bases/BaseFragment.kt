package com.giraffe.triplemapplication.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.giraffe.triplemapplication.R
import com.giraffe.triplemapplication.SharedVM
import com.giraffe.triplemapplication.database.ConcreteLocalSource
import com.giraffe.triplemapplication.model.repo.Repo
import com.giraffe.triplemapplication.network.ApiClient
import com.giraffe.triplemapplication.utils.LoadingDialog
import com.giraffe.triplemapplication.utils.ViewModelFactory

abstract class BaseFragment<VM : ViewModel, B : ViewBinding> : Fragment() {

    protected lateinit var binding: B
    protected lateinit var mViewModel: VM
    protected lateinit var sharedViewModel: SharedVM
    var hasInitializedRootView = false
    private var rootView: View? = null
    private lateinit var loading: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            loading = LoadingDialog(requireActivity())
            binding = getFragmentBinding(inflater, container, false)
            val factory = ViewModelFactory(Repo.getInstance(ApiClient, ConcreteLocalSource(requireContext())))
            mViewModel = ViewModelProvider(this, factory)[getViewModel()]//mViewModel::class.java
            sharedViewModel = ViewModelProvider(requireActivity(), factory)[SharedVM::class.java]
            handleView()
            rootView = binding.root
        } else {
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
        }
        return rootView
    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean): B

    abstract fun handleView()

    override fun onDestroyView() {
        super.onDestroyView()
        loading.cancel()
    }

    fun showLoading() {
        loading.show()
    }

    fun dismissLoading() {
        loading.dismiss()
    }
}