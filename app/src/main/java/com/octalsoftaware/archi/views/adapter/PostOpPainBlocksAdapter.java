package com.octalsoftaware.archi.views.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.models.PostOpPainBlocksModal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by anandj on 4/25/2017.
 */

public class PostOpPainBlocksAdapter  extends RecyclerView.Adapter<PostOpPainBlocksAdapter.MyViewHolder> {

    private List<PostOpPainBlocksModal> moviesList;
    @Nullable
    private Context context = null;
    @NonNull
    private Set<Pair<String,String>> mCheckItem = new HashSet<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox  checkbox_options;

        public MyViewHolder(@NonNull View view) {
            super(view);

            checkbox_options = (CheckBox)view.findViewById(R.id.checkbox_options);

        }
    }
    public PostOpPainBlocksAdapter(List<PostOpPainBlocksModal> moviesList, Context context, @NonNull Set<Pair<String,String>> mCheckItem) {
        this.moviesList = moviesList;
        this.context =  context;
        this.mCheckItem.addAll(mCheckItem);
       // viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_op_pain_block_option_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // add tag to remember
        Pair<String,String> pair = new Pair<String, String>(moviesList.get(position).getName(),String.valueOf(position));
      //  Pair<String,String> pairall = new Pair<String, String>(moviesList.get(position).getName(),context.getString(R.string.no));
        holder.checkbox_options.setTag(pair);

        // set checked if name and position in checked items
        holder.checkbox_options.setChecked(mCheckItem.contains(pair));

        holder.checkbox_options.setText(moviesList.get(position).getName());

        holder.checkbox_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                CheckBox cb = (CheckBox)v;
                Pair<String,String> pair1 = (Pair<String, String>) v.getTag();
                if(cb.isChecked()){
                    mCheckItem.add(pair1);
                }
                else {
                    mCheckItem.remove(pair1);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @NonNull
    public Set<Pair<String,String>> getmCheckItem(){
        return mCheckItem;
    }


}