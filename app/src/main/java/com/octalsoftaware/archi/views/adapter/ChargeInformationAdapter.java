package com.octalsoftaware.archi.views.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.models.ChargeInformationModal;
import com.octalsoftaware.archi.views.activity.chargeinformation.ChargeInformation;

import java.util.List;

/**
 * Created by anandj on 4/19/2017.
 */

public class ChargeInformationAdapter extends RecyclerView.Adapter<ChargeInformationAdapter.MyViewHolder> {

    private List<ChargeInformationModal> moviesList;
    private ChargeInformation activty;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_department, txt_doctorname;
        public RelativeLayout rr_charge_section;
        public TextView txt_below_diagnosis;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txt_department = (TextView) view.findViewById(R.id.txt_department);
            txt_doctorname = (TextView) view.findViewById(R.id.txt_doctorname);
            txt_below_diagnosis = (TextView) view.findViewById(R.id.txt_below_diagnosis);
            rr_charge_section = (RelativeLayout) view.findViewById(R.id.rr_charge_section);

           /* if(getAdapterPosition()==6){
                txt_below_diagnosis.setVisibility(View.VISIBLE);
                txt_doctorname.setVisibility(View.GONE);
            }
            else {
                txt_below_diagnosis.setVisibility(View.GONE);
                txt_doctorname.setVisibility(View.VISIBLE);
            }
*/
            rr_charge_section.setOnClickListener(this);
        }

        @Override
        public void onClick(@NonNull View view) {
            switch (view.getId()) {
                case R.id.rr_charge_section:
                    activty.getChargeInformation(getAdapterPosition());
                    break;
            }
        }
    }

    public ChargeInformationAdapter(List<ChargeInformationModal> moviesList, ChargeInformation activty) {
        this.moviesList = moviesList;
        this.activty = activty;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charge_information_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_department.setText(moviesList.get(position).getName());
        holder.txt_doctorname.setText(moviesList.get(position).getDoctor_name());
        holder.txt_below_diagnosis.setText(moviesList.get(position).getDoctor_name());


        if (position == 6) {
            if (!moviesList.get(position).getDoctor_name().equals("")) {
                holder.txt_below_diagnosis.setVisibility(View.VISIBLE);
                holder.txt_doctorname.setVisibility(View.GONE);
            }
        } else {
            holder.txt_below_diagnosis.setVisibility(View.GONE);
            holder.txt_doctorname.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
