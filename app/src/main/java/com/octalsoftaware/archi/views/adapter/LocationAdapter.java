package com.octalsoftaware.archi.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.models.LocationModal;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.LocationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by anandj on 4/28/2017.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    private List<LocationModal> moviesList;
    @NonNull
    private List<LocationModal> arrayList = new ArrayList<>();
    private LocationActivity context;
    private String api_type = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_hospital_name;
        public RelativeLayout ll_location_search;
        public CheckBox checkbox_system;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txt_hospital_name = (TextView) view.findViewById(R.id.txt_hospital_name);
            ll_location_search = (RelativeLayout) view.findViewById(R.id.ll_location_search);
            checkbox_system = (CheckBox)view.findViewById(R.id.checkbox_system);

            if(!api_type.equals(S.Diagnose))
                checkbox_system.setVisibility(View.GONE);
            else
                checkbox_system.setVisibility(View.VISIBLE);

            ll_location_search.setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View view) {
            switch (view.getId()) {
                case R.id.ll_location_search:
                    String name = "";
                    String id = moviesList.get(getAdapterPosition()).getRef_code();
                    if (!moviesList.get(getAdapterPosition()).getLname().equals("") || !moviesList.get(getAdapterPosition()).getFname().equals(""))
                        name = moviesList.get(getAdapterPosition()).getLname() + " " + moviesList.get(getAdapterPosition()).getFname();
                    else
                        name = moviesList.get(getAdapterPosition()).getTitle();
                      context.filterByName(name,id);
                    break;
            }
        }
    }

    public LocationAdapter(@NonNull List<LocationModal> moviesList, LocationActivity context, String api_type) {
        this.moviesList = moviesList;
        this.context = context;
        this.api_type = api_type;
        arrayList = new ArrayList<>();
        arrayList.addAll(moviesList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_search_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = "";
        if (!moviesList.get(position).getLname().equals("") || !moviesList.get(position).getFname().equals(""))
            name = moviesList.get(position).getLname() + " " + moviesList.get(position).getFname();
        else
            name = moviesList.get(position).getTitle();

        if(!moviesList.get(position).getRef_code().equals(""))
            holder.txt_hospital_name.setText(name + " (" + moviesList.get(position).getRef_code() + ")");
        else
            holder.txt_hospital_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        moviesList.clear();
        if (charText.length() == 0) {
            moviesList.addAll(arrayList);
            //  filter_text = "";
        } else {
            //   filter_text = charText;
            for (LocationModal wp : arrayList) {
              /*  if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText) ) {
                    moviesList.add(wp);
                }*/

            }
        }
        notifyDataSetChanged();
    }
}