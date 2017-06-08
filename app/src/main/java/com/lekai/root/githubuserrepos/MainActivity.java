package com.lekai.root.githubuserrepos;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lekai.root.githubuserrepos.Adapters.RepoAdapter;
import com.lekai.root.githubuserrepos.Networks.MyRepoEndpointInterface;
import com.lekai.root.githubuserrepos.repo.Repo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v7.widget.RecyclerView.LayoutManager;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.username_edittext)
    EditText usernameEditText;
    @InjectView(R.id.search_button)
    Button searchButton;
    @InjectView(R.id.repo_recyclerview)
    RecyclerView repoRecyclerView;
    public String BASE_URL = "https://api.github.com/users/";
    Context myContext;
    List<Repo> repos;
    RepoAdapter repoAdapter;
    LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        myContext = getBaseContext();
        repos = new ArrayList<>();
        if(savedInstanceState != null){
            repos  = savedInstanceState.getParcelableArrayList("repos");
        }
        repoAdapter = new RepoAdapter(myContext,repos);
        layoutManager = new LinearLayoutManager(myContext);
        repoRecyclerView.setHasFixedSize(true);
        repoRecyclerView.setAdapter(repoAdapter);
        repoRecyclerView.setLayoutManager(layoutManager);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                if(username.length() <= 2){
                    Toast.makeText(myContext,"Username cannot be null",Toast.LENGTH_SHORT).show();
                }else {
                    getUserRepos(username);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Repo> repos = this.repos;
        outState.putParcelableArrayList("repos",(ArrayList<Repo>)repos);
    }

    public void getUserRepos(String username){
        Retrofit retrofit;

        retrofit = new Retrofit.Builder()

                .baseUrl(BASE_URL+username+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyRepoEndpointInterface apiService = retrofit.create(MyRepoEndpointInterface.class);

        Call<List<Repo>> call = apiService.getAllRepos();
        call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                if(response.body() instanceof List && response.body() != null) {
                    repos.clear();
                    repos.addAll(response.body());
                    repoAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(myContext,"User could not be found",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                String TAG = "failed to connect";
                Log.e(TAG, t.getMessage());
                Toast.makeText(getBaseContext(), "An error occured, Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
