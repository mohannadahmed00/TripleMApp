package com.giraffe.triplemapplication.repo_testing

import com.giraffe.triplemapplication.model.cart.CartItem
import com.giraffe.triplemapplication.model.categories.CustomCollection
import com.giraffe.triplemapplication.model.products.AllProductsResponse
import com.giraffe.triplemapplication.model.products.Image
import com.giraffe.triplemapplication.model.products.Option
import com.giraffe.triplemapplication.model.products.Product
import com.giraffe.triplemapplication.model.products.Variant
import com.giraffe.triplemapplication.model.wishlist.WishListItem
import com.giraffe.triplemapplication.utils.Constants

object FakeData {

    var isLoggedIn: Boolean = false
    var currentLanguage: Constants.Languages? = null
    var wishListId: Long? = null
    var fullName: String? = null
    var CustomerId: Long? = null
    var CartId: Long? = null
    var currency:String? =  null
    var firstTimeFlag: Boolean = false

    var listOfCartItems : MutableList<CartItem> = mutableListOf()
    var listOfWishListItems : MutableList<WishListItem> = mutableListOf()

    var FakeCartItem : CartItem = CartItem(123L , createProduct() , 1 , false)
    var FakeWishListItem : WishListItem = WishListItem(123L , createProduct() , false)

    var FakeAllProductsResponse : AllProductsResponse = AllProductsResponse(listOf(createProduct() , createProduct()))


    val sampleImage = Image(
        admin_graphql_api_id = "gid://shopify/ProductImage/123",
        created_at = "2023-11-30T12:00:00Z",
        height = 200,
        id = 1,
        position = 1,
        product_id = 12345,
        src = "https://example.com/image.jpg",
        updated_at = "2023-11-30T12:00:00Z",
        width = 300
    )


    val sampleOption = Option(
        id = 1,
        name = "Color",
        position = 1,
        product_id = 12345,
        values = listOf("Red", "Blue", "Green")
    )

    val sampleVariant = Variant(
        admin_graphql_api_id = "gid://shopify/ProductVariant/123",
        compare_at_price = "49.99",
        created_at = "2023-11-30T12:00:00Z",
        fulfillment_service = "manual",
        grams = 500,
        id = 1,
        inventory_item_id = 12345,
        inventory_management = "shopify",
        inventory_policy = "deny",
        inventory_quantity = 100,
        old_inventory_quantity = 90,
        option1 = "Large",
        option2 = "Red",
        position = 1,
        price = "39.99",
        product_id = 54321,
        requires_shipping = true,
        sku = "SKU123",
        taxable = true,
        title = "Large - Red",
        updated_at = "2023-11-30T12:00:00Z",
        weight = 1.2,
        weight_unit = "kg"
    )

    private fun createProduct():Product{

        return Product(
            id = 12345,
            admin_graphql_api_id = "gid://shopify/Product/12345",
            body_html = "<p>This is a sample product description.</p>",
            created_at = "2023-11-30T12:00:00Z",
            handle = "sample-product",
            image = sampleImage,
            images = listOf(sampleImage, sampleImage),
            options = listOf(sampleOption, sampleOption),
            product_type = "Clothing",
            published_at = "2023-11-30T12:00:00Z",
            published_scope = "web",
            status = "active",
            tags = "tag1, tag2, tag3",
            title = "Sample Product",
            updated_at = "2023-11-30T12:00:00Z",
            variants = listOf(sampleVariant, sampleVariant),
            vendor = "Sample Vendor"
        )
    }
}