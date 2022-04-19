package com.aj.hajarialmustafa.model

data class Post(
    val details: Details,
    val images: List<Image>,
    val post_categories: List<PostCategory>,
    val post_id: Int,
    val post_name: String,
    val post_tags: List<PostTag>
)