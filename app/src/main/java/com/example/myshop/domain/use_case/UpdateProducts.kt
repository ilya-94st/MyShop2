package com.example.myshop.domain.use_case

import android.util.Log
import com.example.myshop.common.Constants
import com.example.myshop.domain.models.Products
import com.example.myshop.domain.repository.UpdateRepository
import javax.inject.Inject

class UpdateProducts @Inject constructor(private val updateRepository: UpdateRepository) {

 operator fun invoke(quantity: Int, idProducts: Long) {
     try {
       val productHashMap = mutableMapOf<String, Any>()

           productHashMap[Constants.QUANTITIES_IN_PRODUCTS] = quantity

        updateRepository.upDataProducts(productHashMap, idProducts)
     } catch (e: Exception) {
         Log.e("updateProductsData", "$e")
     }
    }

}

