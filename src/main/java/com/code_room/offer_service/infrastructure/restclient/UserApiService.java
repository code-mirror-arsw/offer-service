package com.code_room.offer_service.infrastructure.restclient;


import com.code_room.offer_service.infrastructure.restclient.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.*;


public interface UserApiService {

    @GET("users/{id}")
    Call<UserDto> findById(@Header("Authorization") String authHeader,@Path("id") String id);



}
