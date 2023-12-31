package com.giraffe.triplemapplication.network

import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.address.AddressResponse
import com.giraffe.triplemapplication.model.address.AddressesResponse
import com.giraffe.triplemapplication.model.brands.AllBrandsResponse
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.response.DraftOrder
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.categories.AllCategoriesResponse
import com.giraffe.triplemapplication.model.currency.ExchangeRatesResponse
import com.giraffe.triplemapplication.model.customers.CustomerDetails
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.MultipleCustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.discount.CouponsResponse
import com.giraffe.triplemapplication.model.orders.AllOrdersResponse
import com.giraffe.triplemapplication.model.orders.OrderResponse
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.model.orders.createorder.createorderresponse.CreateOrderResponse
import com.giraffe.triplemapplication.model.payment.ephemeralkey.EphemeralKeyResponse
import com.giraffe.triplemapplication.model.payment.paymentintent.PaymentIntentResponse
import com.giraffe.triplemapplication.model.payment.stripecustomer.StripeCustomerResponse
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("products.json")
    suspend fun getAllProducts(): AllProductsResponse
    @GET("products.json")
    suspend fun getAllProductsFromIds(@Query("ids") ids: String): AllProductsResponse
    @GET("v1/latest")
    suspend fun getExchangeRates(
        @Query("access_key") accessKey: String="4ee6d3381b90ee1d4e7a0c551205269f",
        @Query("symbols") symbols: String = "USD,EUR,GBP,EGP",
        @Query("format") format: Int = 1
    ): ExchangeRatesResponse

    @GET("custom_collections.json")
    suspend fun getAllCategories(): AllCategoriesResponse

    @GET("smart_collections.json")
    suspend fun getAllBrands(): AllBrandsResponse

    @POST("customers/{customer_id}/addresses.json")
    suspend fun addNewAddress(
        @Path("customer_id") customerId:String,
        @Body address: AddressRequest
    ):Response<AddressResponse>

    @GET("customers/{customer_id}/addresses.json")
    suspend fun getAddresses(
        @Path("customer_id") customerId:String,
    ):Response<AddressesResponse>

    @DELETE("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun deleteAddress(
        @Path("customer_id") customerId:String,
        @Path("address_id") addressId:String,
    ):Response<Void>
    @GET("collections/{categoryId}/products.json")
    suspend fun getProductsFromCategoryId(@Path("categoryId") categoryId: String): AllProductsResponse

    @GET("orders.json")
    suspend fun getOrders(@Query("status") status: String = "any"): AllOrdersResponse

    @GET("orders/{orderId}.json")
    suspend fun getOrder(@Path("orderId") orderId: Long): OrderResponse

    @POST("orders.json")
    suspend fun createOrder(@Body orderCreate: OrderCreate): CreateOrderResponse

    @DELETE("orders/{orderId}/.json")
    suspend fun delOrder(@Path("orderId") orderId: Long)

    @PUT("draft_orders/{orderId}/complete.json")
    suspend fun completeOrder(@Path("orderId") orderId: Long): DraftOrder
//    @GET
//    suspend fun getCurrencies(): CurrencyResponse
    /*@POST("orders.json")
    suspend fun createOrder(@Query("") orderCreation: OrderCreation)*/

    @DELETE("orders/{orderId}/.json")
    suspend fun delOrder(@Path("orderId") orderId: String)

    @POST("customers.json")
    suspend fun createCustomer(@Body customer : Request) : CustomerResponse

    //===================draft work area===================
    @POST("draft_orders.json")
    suspend fun createNewDraftOrder(@Body draftRequest: DraftRequest):Response<DraftResponse>

    @PUT("draft_orders/{draft_order_id}.json")
    suspend fun modifyDraftOrder(
        @Path("draft_order_id") draftOrderId:Long,
        @Body draftRequest: DraftRequest):Response<DraftResponse>

    @DELETE("draft_orders/{draft_order_id}.json")
    suspend fun removeDraftOrder(
        @Path("draft_order_id") draftOrderId:Long
    ):Response<Void>

    @GET("price_rules.json")
    suspend fun getCoupons():Response<CouponsResponse>

    @PUT("customers/{customer_id}/addresses/{address_id}/default.json")
    suspend fun setDefaultAddress(
        @Path("customer_id") customerId:Long,
        @Path("address_id") addressId:Long):Response<AddressResponse>
    @GET("customers.json")
    suspend fun getCustomerByEmail(@Query("email") email:String) :MultipleCustomerResponse

    @GET("customers/{customerId}.json")
    suspend fun getCustomerById(@Path("customerId") customerId: Long): CustomerDetails
    @POST("customers")
    suspend fun createStripeCustomer():Response<StripeCustomerResponse>

    @POST("ephemeral_keys")
    suspend fun createEphemeralKey(@Query("customer") customerId: String):Response<EphemeralKeyResponse>

    @POST("payment_intents")
    @FormUrlEncoded
    suspend fun createPaymentIntent(
        @Field("customer") customerId: String,
        @Field("amount") amount: String,
        @Field("currency") currency: String,
        @Field("automatic_payment_methods[enabled]") automaticPaymentMethodsEnabled: Boolean = true
    ):Response<PaymentIntentResponse>



    @GET("draft_orders/{cart_id}.json")
    suspend fun getSingleCart(@Path("cart_id") cartId: Long): Response<DraftResponse>
    @GET("draft_orders/{wish_id}.json")
    suspend fun getSingleWish(@Path("wish_id") wishId: Long): Response<DraftResponse>
    @GET("products.json")
    suspend fun getListOfProducts(@Query("ids") ids: String): Response<AllProductsResponse>
}