package com.lekai.root.githubuserrepos.Networks;

import com.lekai.root.githubuserrepos.repo.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by root on 6/8/17.
 */

public interface MyRepoEndpointInterface {
    final String API_KEY = "85489f7fdeb964a14917893c8b1d08235227f13f";
    @GET("repos?api_key="+API_KEY)
    Call<List<Repo>> getAllRepos();
}
