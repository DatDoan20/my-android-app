package com.doanducdat.shoppingapp.room.entity

import com.doanducdat.shoppingapp.model.product.Product

fun Product.toProductCacheEntity(): ProductCacheEntity {
    return ProductCacheEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        imageCover = this.getImageCover(),
        slug = this.slug,
        price = this.getUnFormatPrice(),
        brand = this.brand,
        size = this.size,
        color = this.color,
        material = this.material,
        pattern = this.pattern,
        discount = this.getUnFormatDiscount(),
        outOfStock = this.outOfStock,
        type = this.type,
        category = this.category,
        ratingsAverage = this.ratingsAverage,
        ratingsQuantity = this.getUnFormatRatingsQuantity(),
        createdAt = this.createdAt
    )
}

fun ProductCacheEntity.toProduct(images: List<String>): Product {
    return Product(
        id = this.id,
        name = this.name,
        description = this.description,
        images = images,
        imageCover = this.imageCover,
        slug = this.slug,
        price = this.price,
        brand = this.brand,
        size = this.size,
        color = this.color,
        material = this.material,
        pattern = this.pattern,
        discount = this.discount,
        outOfStock = this.outOfStock,
        type = this.type,
        category = this.category,
        ratingsAverage = this.ratingsAverage,
        ratingsQuantity = this.ratingsQuantity,
        createdAt = this.createdAt
    )
}

fun List<Product>.toListProductCacheEntity(): List<ProductCacheEntity> {
    return this.map { it.toProductCacheEntity() }
}

fun List<ProductCacheEntity>.toListProduct(imagesAllProduct: List<ImageCacheEntity>): List<Product> {
    return this.map { itemProductCache ->

        val images: List<String> = imagesAllProduct.filter { itemImageCache ->
            itemProductCache.id == itemImageCache.id
        }.map {
            it.name
        }
        itemProductCache.toProduct(images)
    }
}