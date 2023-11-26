package com.giraffe.triplemapplication.network


import android.util.Log
import com.giraffe.triplemapplication.model.address.AddressRequest
import com.giraffe.triplemapplication.model.cart.request.DraftOrder
import com.giraffe.triplemapplication.model.cart.request.DraftRequest
import com.giraffe.triplemapplication.model.cart.request.LineItem
import com.giraffe.triplemapplication.model.cart.response.DraftResponse
import com.giraffe.triplemapplication.model.customers.CustomerResponse
import com.giraffe.triplemapplication.model.customers.Request
import com.giraffe.triplemapplication.model.orders.createorder.OrderCreate
import com.giraffe.triplemapplication.utils.Constants
import com.giraffe.triplemapplication.utils.await
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient : RemoteSource {
    private const val TAG = "ApiClient"

    private fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(Constants.SHOPIFY_HEADER, Constants.ACCESS_TOKEN)
                .build()
            chain.proceed(request)
        }
        return httpClient.build()
    }

    private fun getApiServices(url: String = Constants.URL): ApiServices {
        val apiServices = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
        if (url == Constants.URL) {
            apiServices.client(provideOkHttpClient())
        }
        return apiServices.build().create(ApiServices::class.java)
    }


    override suspend fun getAllProducts() = flow {
        emit(getApiServices().getAllProducts())
    }

    override suspend fun getCurrencies() = flow {
        emit(getApiServices(Constants.CURRENCY_URL).getExchangeRates())
    }

    override fun getCustomerByEmail(email: String): Flow<CustomerResponse> =flow{
        emit(getApiServices().getCustomerByEmail(email))
    }

    override suspend fun getAllCategories() = flow {
        emit(getApiServices().getAllCategories())
    }

    override suspend fun getAllBrands() = flow {
        emit(getApiServices().getAllBrands())

    }

    override suspend fun getProductsFromCategoryId(categoryId: String) = flow {
        var productsIds = ""
        val products = getApiServices().getProductsFromCategoryId(categoryId).products
        products.forEachIndexed { index, product ->
            productsIds += if (index == products.size - 1) {
                product.id
            } else {
                "${product.id}, "
            }
        }
        emit(getApiServices().getAllProductsFromIds(productsIds))
    }

    override suspend fun createOrder(orderCreate: OrderCreate) = flow {
        emit(getApiServices().createOrder(orderCreate))
    }

    override suspend fun getOrders() = flow {
        emit(getApiServices().getOrders())
    }

    override suspend fun delOrder(orderId: Long) {
        getApiServices().delOrder(orderId)
    }

    override suspend fun createNewCartDraft(cartItems: List<LineItem>): Flow<Response<DraftResponse>> {
        return flow {
            emit(getApiServices().createNewDraftOrder(DraftRequest(DraftOrder(line_items = cartItems))))
        }
    }

    override suspend fun modifyCartDraft(
        draftOrderId: Long,
        cartItems: List<LineItem>,
    ): Flow<Response<DraftResponse>> {
        return flow {
            emit(
                getApiServices().modifyDraftOrder(
                    draftOrderId,
                    DraftRequest(DraftOrder(draftOrderId, cartItems))
                )
            )
        }
    }

    override suspend fun removeCartDraft(
        draftOrderId: Long,
    ): Flow<Response<Void>> {
        return flow {
            emit(getApiServices().removeDraftOrder(draftOrderId))
        }
    }

    override suspend fun addNewAddress(
        customerId: String,
        address: AddressRequest,
    ) = flow {
        emit(getApiServices().addNewAddress(customerId, address))
    }

    override suspend fun getAddresses(
        customerId: String,
    ) = flow {
        emit(getApiServices().getAddresses(customerId))
    }

    override suspend fun deleteAddress(
        customerId: String,
        addressId: String,
    ) = flow {
        emit(getApiServices().deleteAddress(customerId, addressId))
    }

    override fun signUpFirebase(
        email: String,
        password: String,

        ): Flow<AuthResult> = flow {
        val result = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        emit(result)
    }

    override fun signInFirebase(
        email: String,
        password: String,
    ): Flow<AuthResult> = flow {
        val result =FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        emit(result)
    }


    override fun getCurrentUser(): FirebaseUser {

        return FirebaseAuth.getInstance().currentUser!!
    }

    override fun isLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun createCustomer(customerResponse: Request): Flow<CustomerResponse> = flow {
        emit(getApiServices().createCustomer(customerResponse))
    }



    override suspend fun uploadCartId(cartId: Long): Task<Void?>? {
        var mTask: Task<Void?>? = null
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(
                hashMapOf(
                    "cartId" to cartId,//cart id from shopify
                )
            )
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Log.i(TAG, "cart id have been uploaded")
                } else {
                    Log.e(
                        TAG,
                        "cart id have not been uploaded => ${task.exception?.message} => ${task.result}"
                    )
                }
                mTask = task
            }
        return mTask
    }

    override suspend fun getCartId(): Flow<Long> {
        return flow {
            val result = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)//?????????
                .get().await().getLong("cartId")
            if (result != null) {
                emit(result)
                Log.i(
                    TAG,
                    "getting cart id successfully $result"
                )
            } else {
                Log.e(
                    TAG,
                    "getting cart id fail"
                )
            }
        }
    }

    override suspend fun uploadCustomerId(customerId: Long): Task<Void?>? {
        var mTask: Task<Void?>? = null
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(
                hashMapOf(
                    "customerId" to customerId,
                )
            )
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Log.i(TAG, "customer id have been uploaded")
                } else {
                    Log.e(
                        TAG,
                        "customer id have not been uploaded => ${task.exception?.message} => ${task.result}"
                    )
                }
                mTask = task
            }
        return mTask
    }

    override suspend fun getCustomerId(): Flow<Long> {
        return flow {
            val result = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get().await().getLong("customerId")
            if (result != null) {
                emit(result)
                Log.i(
                    TAG,
                    "getting customer id successfully $result"
                )
            } else {
                Log.e(
                    TAG,
                    "getting customer id fail"
                )
            }
        }
    }

    override suspend fun createNewWishListDraft(productsItem: List<LineItem>): Flow<Response<DraftResponse>> {
        return flow {
            emit(getApiServices().createNewDraftOrder(DraftRequest(DraftOrder(line_items = productsItem))))
        }
    }

    override suspend fun modifyWishListDraft(
        draftOrderId: Long,
        products: List<LineItem>,
    ): Flow<Response<DraftResponse>> {
        return flow {
            emit(
                getApiServices().modifyDraftOrder(
                    draftOrderId,
                    DraftRequest(DraftOrder(draftOrderId, products))
                )
            )
        }
    }

    override suspend fun removeWishListDraft(draftOrderId: Long): Flow<Response<Void>> {
        return flow {
            emit(getApiServices().removeDraftOrder(draftOrderId))
        }
    }

    override suspend fun uploadWishListId(wishListId: Long): Task<Void?>? {
        var mTask: Task<Void?>? = null
        FirebaseFirestore.getInstance().collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(
                hashMapOf(
                    "wishListId" to wishListId,
                )
            )
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Log.i(TAG, "cart id have been uploaded")
                } else {
                    Log.e(
                        TAG,
                        "cart id have not been uploaded => ${task.exception?.message} => ${task.result}"
                    )
                }
                mTask = task
            }
        return mTask
    }

    override suspend fun getWishListId(): Flow<Long> {
        return flow {
            val result = FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get().await().getLong("wishListId")
            if (result != null) {
                emit(result)
                Log.i(
                    TAG,
                    "getting wish list id successfully $result"
                )
            } else {
                Log.e(
                    TAG,
                    "getting wish list id fail"
                )
            }
        }
    }

}