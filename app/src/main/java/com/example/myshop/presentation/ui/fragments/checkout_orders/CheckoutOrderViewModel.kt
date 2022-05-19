package com.example.myshop.presentation.ui.fragments.checkout_orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshop.common.EventClass
import com.example.myshop.domain.models.ProductsInCart
import com.example.myshop.domain.models.ProductsInOrder
import com.example.myshop.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CheckoutOrderViewModel @Inject constructor(
    private val getProductInCart: GetProductInCart,
    private val deleteAllProductsInCart: DeleteAllProductsInCart,
    private val addProductInOrder: AddProductInOrder,
    private val updateProducts: UpdateProducts,
    private val getProductIdProduct: GetProductIdProduct
    ): ViewModel() {

    private lateinit var listIdProducts: MutableList<Long>

    private lateinit var quantityResult: MutableList<Int>

    private lateinit var listProducts: MutableList<Int>

    private lateinit var listProductsInCart: MutableList<Int>


    private var _time = MutableLiveData<String>()

    var time: LiveData<String> = _time

    private var _result = MutableLiveData<EventClass>()

    var result: LiveData<EventClass> = _result

    private var _allPrice = MutableLiveData<Float>()

    var allPrice: LiveData<Float> = _allPrice

    fun getProductInCart(idBuyer: String) = viewModelScope.launch {
       _result.postValue(getProductInCart.invoke(idBuyer))
    }

    fun getAllPriceInCart(productInCart: MutableList<ProductsInCart>) {
        var priceAll = 0F
        for (product in productInCart) {
            val price = product.price
            val quantity = product.quantity
            priceAll += price * quantity
        }
        _allPrice.value = priceAll
    }

    fun deleteProducts(idBuyer: String) {
        deleteAllProductsInCart.invoke(idBuyer)
    }

    fun addProductInOrder(productsInOrder: ProductsInOrder) = viewModelScope.launch {
        addProductInOrder.invoke(productsInOrder)
    }

    fun getProduct(idProducts: Long, productsInCart: MutableList<ProductsInCart>) = viewModelScope.launch {
          listProducts = mutableListOf()

           listProductsInCart = mutableListOf()

           listIdProducts = mutableListOf()

          getProductIdProduct.invoke(idProducts).forEach {
              val q = it.quantity
              val id = it.idProducts
              if (q != null) {
                  listProducts.add(q)
                  listIdProducts.add(id)
              }
        }
         productsInCart.forEach {
            listProductsInCart.add(it.quantity)
        }

    }

    init {
        currentDate()
    }

     fun updateResult() {
        val result = mutableListOf<Int>()
         quantityResult = mutableListOf()
          for(i in listProducts.indices) {
               result.add(listProducts[i] - listProductsInCart[i])
                quantityResult = result
             }
         for(i in 0 until quantityResult.size) {
             updateProducts.invoke(quantityResult[i], listIdProducts[i])
         }
    }

 private  fun currentDate(){
        val calender = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
         _time.value = dateFormat.format(calender.time)
    }
}