package com.lekai.root.githubuserrepos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lekai.root.githubuserrepos.R;
import com.lekai.root.githubuserrepos.repo.Repo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by root on 6/8/17.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoAdapterViewholder> {
    Context myContext;
    List<Repo> myRepos;
    @Override
    public RepoAdapter.RepoAdapterViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =  LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.each_repo_view,parent,false);
        return new RepoAdapterViewholder(view);
    }

    public RepoAdapter(Context context, List<Repo> repoList){
        myContext = context;
        myRepos = repoList;
    }
    @Override
    public void onBindViewHolder(RepoAdapter.RepoAdapterViewholder holder, int position) {
        String name = myRepos.get(position).getName().toString();
        String dateUpdated = "Last Updated : "+ myRepos.get(position).getUpdatedAt().toString().substring(0,10);
        String language = "Language : "+ myRepos.get(position).getLanguage();
        String forkNumber = "Forks : "+ myRepos.get(position).getForks().toString();
        holder.nameTextView.setText(name);
        holder.dateTextView.setText(dateUpdated);
        holder.languageTextView.setText(language);
        holder.forkNumberTextView.setText(forkNumber);
        final String repoLink = myRepos.get(position).getHtmlUrl();
        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(repoLink);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(intent.resolveActivity(myContext.getPackageManager())!=null){
                    myContext.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return myRepos.size();
    }

    public class RepoAdapterViewholder extends RecyclerView.ViewHolder {
        @InjectView(R.id.repo_name_textview)
        TextView nameTextView;
        @InjectView(R.id.date_updated_textview)
        TextView dateTextView;
        @InjectView(R.id.language_textview)
        TextView languageTextView;
        @InjectView(R.id.fork_number_textview)
        TextView forkNumberTextView;
        public RepoAdapterViewholder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
