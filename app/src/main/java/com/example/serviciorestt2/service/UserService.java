package com.example.serviciorestt2.service;

import com.example.serviciorestt2.entity.Productos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
public interface UserService {

    @GET("products")
    public abstract Call<List<Productos>> listaProductos();

}
