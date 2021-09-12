package com.doanducdat.shoppingapp.ui.main.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doanducdat.shoppingapp.R
import com.doanducdat.shoppingapp.adapter.SlideImageSmallProductAdapter
import com.doanducdat.shoppingapp.databinding.FragmentProductPhotoViewBinding
import com.doanducdat.shoppingapp.ui.base.BaseFragment


class ProductPhotoViewFragment : BaseFragment<FragmentProductPhotoViewBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductPhotoViewBinding =
        FragmentProductPhotoViewBinding.inflate(inflater, container, false)

    private val controller by lazy {
        (requireActivity().supportFragmentManager
            .findFragmentById(R.id.container_main) as NavHostFragment).findNavController()
    }
    var urlImages: MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackFragment()
        getDataFromAnotherFragment()
        setUpRcvImagesProduct()
    }

    private fun setUpBackFragment() {
        binding.imgBack.setOnClickListener {
            controller.popBackStack()
        }
    }

    private fun getDataFromAnotherFragment() {
        val bundle = arguments
        if (bundle != null) {
            urlImages = bundle.getStringArrayList("URL_IMAGES")!!
            showToast(urlImages.size.toString())
        }
    }

    private fun setUpRcvImagesProduct() {
        val urlImagesAdapter: SlideImageSmallProductAdapter = SlideImageSmallProductAdapter()
        urlImagesAdapter.setUrlImagesProduct(urlImages)
        urlImagesAdapter.mySetOnclickImage {
            binding.productPhotoView.setImageDrawable(it)
        }

        binding.rcvImgProductToPhotoView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvImgProductToPhotoView.adapter = urlImagesAdapter

    }
}