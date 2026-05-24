package com.example.sponsorhub.core.network

import com.example.sponsorhub.data.model.Article
import com.example.sponsorhub.data.model.Events
import com.example.sponsorhub.data.model.Product
import com.example.sponsorhub.data.model.Review
import com.example.sponsorhub.data.model.SponsorshipRequest
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.remote.request.CreateArticleRequest
import com.example.sponsorhub.data.remote.request.CreateEventRequest
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

    // ─── Articles ─────────────────────────────────────────────────────────────

    @GET("articles")
    suspend fun getArticles(): List<Article>

    @GET("articles")
    suspend fun getArticleById(
        @Query("id") id: String          // format: "eq.<uuid>"
    ): List<Article>

    @POST("articles")
    suspend fun createArticle(
        @Body body: CreateArticleRequest
    ): Response<Unit>

    @DELETE("articles")
    suspend fun deleteArticle(
        @Query("id") id: String          // format: "eq.<uuid>"
    ): Response<Unit>

    // ─── Events ───────────────────────────────────────────────────────────────

    @GET("events")
    suspend fun getEvents(): List<Events>

    @GET("events")
    suspend fun getEventsByCreator(
        @Query("created_by") createdBy: String   // format: "eq.<uuid>"
    ): List<Events>

    @GET("events")
    suspend fun getEventById(
        @Query("id") id: String                  // format: "eq.<uuid>"
    ): List<Events>

    @POST("events")
    suspend fun createEvent(
        @Body body: CreateEventRequest
    ): Response<Unit>

    @DELETE("events")
    suspend fun deleteEvent(
        @Query("id") id: String                  // format: "eq.<uuid>"
    ): Response<Unit>

    // ─── Products ─────────────────────────────────────────────────────────────

    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products")
    suspend fun getProductById(
        @Query("id") id: String                  // format: "eq.<uuid>"
    ): List<Product>

    @POST("products")
    suspend fun createProduct(
        @Body body: CreateProductRequest
    ): Response<Unit>

    @DELETE("products")
    suspend fun deleteProduct(
        @Query("id") id: String                  // format: "eq.<uuid>"
    ): Response<Unit>

    // ─── Reviews ──────────────────────────────────────────────────────────────

    @GET("reviews")
    suspend fun getReviewBySponsorship(
        @Query("sponsorship_id") sponsorshipId: String   // format: "eq.<uuid>"
    ): List<Review>

    @POST("reviews")
    suspend fun createReview(
        @Body body: CreateReviewRequest
    ): Response<Unit>

    // ─── Sponsorship Requests ─────────────────────────────────────────────────

    @GET("sponsorship_requests")
    suspend fun getRequestsByEvent(
        @Query("event_id") eventId: String               // format: "eq.<uuid>"
    ): List<SponsorshipRequest>

    @GET("sponsorship_requests")
    suspend fun getUserRequestForEvent(
        @Query("event_id") eventId: String,              // format: "eq.<uuid>"
        @Query("umkm_id") umkmId: String                 // format: "eq.<uuid>"
    ): List<SponsorshipRequest>

    @POST("sponsorship_requests")
    suspend fun createSponsorshipRequest(
        @Body body: CreateSponsorshipRequest
    ): Response<Unit>

    @PATCH("sponsorship_requests")
    suspend fun updateRequestStatus(
        @Query("id") id: String,                         // format: "eq.<uuid>"
        @Body body: UpdateStatusRequest
    ): Response<Unit>

    // ─── Users ────────────────────────────────────────────────────────────────

    @GET("users")
    suspend fun getUserById(
        @Query("id") id: String                          // format: "eq.<uuid>"
    ): List<User>

    @PATCH("users")
    suspend fun updateProfileImage(
        @Query("id") id: String,                         // format: "eq.<uuid>"
        @Body body: UpdateProfileImageRequest
    ): Response<Unit>

    @POST("users")
    suspend fun createUser(
        @Body body: CreateUserRequest
    ): Response<Unit>
}
