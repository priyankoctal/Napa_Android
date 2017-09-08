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
import com.octalsoftaware.archi.views.activity.chargeinformation.DiagnosisActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anandj on 5/4/2017.
 */

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.MyViewHolder> {

    private List<LocationModal> moviesList;
    private DiagnosisActivity context;
    private String api_type = "";
    @NonNull
    private ArrayList<String> check_id = new ArrayList<>();
    @NonNull
    private HashMap<String,String> check_name = new HashMap<>();
   // private HashSet<String,LocationModal> check_map_id = new HashSet<>();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_hospital_name;
        public RelativeLayout ll_location_search;
        public CheckBox checkbox_system;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txt_hospital_name = (TextView) view.findViewById(R.id.txt_hospital_name);
            ll_location_search = (RelativeLayout) view.findViewById(R.id.ll_location_search);
            checkbox_system = (CheckBox)view.findViewById(R.id.checkbox_system);


            //ll_location_search.setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View view) {
            switch (view.getId()) {
                case R.id.ll_location_search:
                    String name = "";
                    String id = moviesList.get(getAdapterPosition()).getId();
                    if (!moviesList.get(getAdapterPosition()).getLname().equals("") || !moviesList.get(getAdapterPosition()).getFname().equals(""))
                        name = moviesList.get(getAdapterPosition()).getLname() + " " + moviesList.get(getAdapterPosition()).getFname();
                    else
                        name = moviesList.get(getAdapterPosition()).getTitle();
                 //   context.filterByName(name,getCheck_id());
                    break;
            }
        }
    }

    public DiagnosisAdapter(List<LocationModal> moviesList, DiagnosisActivity context, String api_type, @NonNull HashMap<String,String> check_id) {
        this.moviesList = moviesList;
        this.context = context;
        this.api_type = api_type;
        check_name.putAll(check_id);
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

        holder.checkbox_system.setTag(String.valueOf(position));

        holder.checkbox_system.setChecked(check_name.containsKey(moviesList.get(position).getId()));


        holder.checkbox_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox)view;
                String id = (String) checkBox.getTag();
                moviesList.get(Integer.parseInt(id)).getId();
                if(checkBox.isChecked()){
                    check_name.put(moviesList.get(Integer.parseInt(id)).getId(), moviesList.get(Integer.parseInt(id)).getTitle());
                    //check_id.add(id);
                }
                else {
                    check_name.remove(moviesList.get(Integer.parseInt(id)).getId());
                 //   check_id.remove(id);
                }
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    public HashMap<String,String> getCheck_id(){
        return check_name;
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
