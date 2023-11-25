package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.address.AddressResponse
import com.giraffe.triplemapplication.model.address.AddressesResponse

import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request

import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse

import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.orders.AllOrdersResponse
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.createorderresponse.CreateOrderResponse


import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {


    suspend fun getAllProducts(): Flow<AllProductsResponse>
    suspend fun getCurrencies(): Flow<ExchangeRatesResponse>


    fun signUpFirebase(


        email: String,
        password: String,
        ): Flow<AuthResult>


     fun signInFirebase(email: String, password: String): Flow<AuthResult>

    fun getCurrentUser(): FirebaseUser
    fun isLoggedIn(): Boolean
    fun logout()
    fun createCustomer(customer: Request):Flow<CustomerResponse>
    suspend fun getAllCategories(): Flow<AllCategoriesResponse>
    suspend fun getAllBrands(): Flow<AllBrandsResponse>

    suspend fun addNewAddress(
        customerId:String,
        address: AddressRequest
    ): Flow<Response<AddressResponse>>

    suspend fun getAddresses(
        customerId:String,
    ):Flow<Response<AddressesResponse>>

    suspend fun deleteAddress(
        customerId:String,
        addressId:String,
    ):Flow<Response<Void>>
    suspend fun getProductsFromCategoryId(categoryId: String): Flow<AllProductsResponse>

    suspend fun createOrder(orderCreate: OrderCreate): Flow<CreateOrderResponse>
    suspend fun getOrders(): Flow<AllOrdersResponse>
    suspend fun delOrder(orderId: Long)

}