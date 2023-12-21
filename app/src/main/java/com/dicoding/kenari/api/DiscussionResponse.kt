package com.dicoding.kenari.api

import com.squareup.moshi.Json

data class GetAllDiscussionsResponse(
    @Json(name = "message")
    val message: String? = null,

    @Json(name = "data")
    val data: DiscussionData? = null
)

data class DiscussionData(
    @Json(name = "discussions")
    val discussions: List<Discussion>? = null
)

data class Discussion(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "isAnonymous")
    val isAnonymous: String? = null,

    @Json(name = "content")
    val content: String? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "user")
    val user: UserDataDiscussion? = null
)

data class UserDataDiscussion(
    @Json(name = "user_id")
    val userId: String? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "email")
    val email: String? = null,

    @Json(name = "label")
    val label: String? = null,

    @Json(name = "profile_photo")
    val profilePhoto: String? = null
)

//  Get Discussions Detail By ID
data class GetDiscussionByIdResponse(
    @Json(name = "message")
    val message: String? = null,

    @Json(name = "data")
    val data: DiscussionDetailData? = null
)

data class DiscussionDetailData(
    @Json(name = "discussion")
    val discussion: DiscussionDetail? = null,

    @Json(name = "comments")
    val comments: List<Comment>? = null
)

data class DiscussionDetail(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "isAnonymous")
    val isAnonymous: String? = null,

    @Json(name = "content")
    val content: String? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "user")
    val user: UserDataDiscussion? = null
)

data class Comment(
    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "discussion_id")
    val discussionId: String? = null,

    @Json(name = "comment")
    val comment: String? = null,

    @Json(name = "createdAt")
    val createdAt: String? = null,

    @Json(name = "user")
    val user: UserDataDiscussion? = null
)

