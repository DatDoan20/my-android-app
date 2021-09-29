package com.doanducdat.shoppingapp.ui.main.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
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
        }
    }

    private fun setUpRcvImagesProduct() {
        val urlImagesAdapter: SlideImageSmallProductAdapter = SlideImageSmallProductAdapter()
        urlImagesAdapter.setUrlImagesProduct(urlImages)
        //set default photoView is first item of rcv
        binding.productPhotoView.load(urlImagesAdapter.getFirstUrlImage())

        // event click -> show image to photoView
        urlImagesAdapter.mySetOnclickImage { layoutImage, image ->
            layoutImage.strokeWidth = 8
            binding.productPhotoView.setImageDrawable(image)
        }

        // event click -> change border of item in rcv
        binding.rcvImgProductToPhotoView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcvImgProductToPhotoView.adapter = urlImagesAdapter

    }
}