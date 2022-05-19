package com.example.myshop.domain.use_case

import com.example.myshop.domain.repository.ProductsRepository
import javax.inject.Inject

class DeleteAddress @Inject constructor(private val productsRepository: ProductsRepository) {

     operator fun invoke(idAddress: Long) {
        productsRepository.deleteAddress(idAddress)
    }
}