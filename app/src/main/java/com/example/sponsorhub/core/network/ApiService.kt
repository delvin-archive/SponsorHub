package com.example.sponsorhub.core.network

import com.example.sponsorhub.data.model.Article
import com.example.sponsorhub.data.model.Events
import com.example.sponsorhub.data.model.Favorite
import com.example.sponsorhub.data.model.Product
import com.example.sponsorhub.data.model.Review
import com.example.sponsorhub.data.model.SponsorshipRequest
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.remote.request.CreateArticleRequest
import com.example.sponsorhub.data.remote.request.CreateEventRequest
import com.example.sponsorhub.data.remote.request.CreateFavoriteRequest
import com.example.sponsorhub.data.remote.request.CreateProductRequest
import com.example.sponsorhub.data.remote.request.CreateReviewRequest
import com.example.sponsorhub.data.remote.request.CreateSponsorshipRequest
import com.example.sponsorhub.data.remote.request.CreateUserRequest
import com.example.sponsorhub.data.remote.request.UpdateProfileImageRequest
import com.example.sponsorhub.data.remote.request.UpdateStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("articles")
    suspend fun getArticles(): List<Article>

    @GET("articles")
    suspend fun getArticleById(
        @Query("id") id: String
    ): List<Article>

    @POST("articles")
    suspend fun createArticle(
        @Body body: CreateArticleRequest
    ): Response<Unit>

    @DELETE("articles")
    suspend fun deleteArticle(
        @Query("id") id: String
    ): Response<Unit>

    @GET("events")
    suspend fun getEvents(): List<Events>

    @GET("events")
    suspend fun getEventsByCreator(
        @Query("created_by") createdBy: String
    ): List<Events>

    @GET("events")
    suspend fun getEventById(
        @Query("id") id: String
    ): List<Events>

    @POST("events")
    suspend fun createEvent(
        @Body body: CreateEventRequest
    ): Response<Unit>

    @DELETE("events")
    suspend fun deleteEvent(
        @Query("id") id: String
    ): Response<Unit>

    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products")
    suspend fun getProductById(
        @Query("id") id: String
    ): List<Product>

    @GET("products")
    suspend fun getProductsByUser(
        @Query("user_id") userId: String
    ): List<Product>

    @POST("products")
    suspend fun createProduct(
        @Body body: CreateProductRequest
    ): Response<Unit>

    @DELETE("products")
    suspend fun deleteProduct(
        @Query("id") id: String
    ): Response<Unit>

    @GET("reviews")
    suspend fun getReviewBySponsorship(
        @Query("sponsorship_id") sponsorshipId: String
    ): List<Review>

    @POST("reviews")
    suspend fun createReview(
        @Body body: CreateReviewRequest
    ): Response<Unit>

    @GET("sponsorship_requests")
    suspend fun getRequestsByEvent(
        @Query("event_id") eventId: String
    ): List<SponsorshipRequest>

    @GET("sponsorship_requests")
    suspend fun getSponsorshipById(
        @Query("id") id: String
    ): List<SponsorshipRequest>

    @GET("sponsorship_requests")
    suspend fun getUserRequestForEvent(
        @Query("event_id") eventId: String,
        @Query("umkm_id") umkmId: String
    ): List<SponsorshipRequest>

    @POST("sponsorship_requests")
    suspend fun createSponsorshipRequest(
        @Body body: CreateSponsorshipRequest
    ): Response<Unit>

    @PATCH("sponsorship_requests")
    suspend fun updateRequestStatus(
        @Query("id") id: String,
        @Body body: UpdateStatusRequest
    ): Response<Unit>

    @GET("users")
    suspend fun getUserById(
        @Query("id") id: String
    ): List<User>

    @PATCH("users")
    suspend fun updateProfileImage(
        @Query("id") id: String,
        @Body body: UpdateProfileImageRequest
    ): Response<Unit>

    @POST("users")
    suspend fun createUser(
        @Body body: CreateUserRequest
    ): Response<Unit>

    @GET("favorites")
    suspend fun getFavoritesByUmkm(
        @Query("umkm_id") umkmId: String
    ): List<Favorite>

    @GET("favorites")
    suspend fun checkFavorite(
        @Query("panitia_id") panitiaId: String,
        @Query("umkm_id") umkmId: String
    ): List<Favorite>

    @POST("favorites")
    suspend fun addFavorite(
        @Body body: CreateFavoriteRequest
    ): Response<Unit>
}
