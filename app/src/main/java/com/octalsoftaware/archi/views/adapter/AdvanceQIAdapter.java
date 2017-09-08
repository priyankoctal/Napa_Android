package com.octalsoftaware.archi.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.models.AdvancedQIModal;
import com.octalsoftaware.archi.utils.constants.S;
import com.octalsoftaware.archi.views.activity.qualityinformation.CardiovascularActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.ComplianceActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.MorbidityMortality;
import com.octalsoftaware.archi.views.activity.qualityinformation.NeurologicActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.PatientSafety;
import com.octalsoftaware.archi.views.activity.qualityinformation.PharmacyBloodBank;
import com.octalsoftaware.archi.views.activity.qualityinformation.ProceduralActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.QIAirwayRespiratoryActivity;
import com.octalsoftaware.archi.views.activity.qualityinformation.RegionalActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anandj on 4/18/2017.
 */

public class AdvanceQIAdapter extends RecyclerView.Adapter<AdvanceQIAdapter.MyViewHolder> {

    private List<AdvancedQIModal> moviesList;
    @NonNull
    private ArrayList<AdvancedQIModal> mSavedQI = new ArrayList<>();
    @Nullable
    private Context context = null;
    private String patient_id = "";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_notificationtext;
        public RelativeLayout rr_advanced_qi;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txt_notificationtext = (TextView) view.findViewById(R.id.txt_settingname);
            rr_advanced_qi = (RelativeLayout)view.findViewById(R.id.rr_advanced_qi);

            rr_advanced_qi.setOnClickListener(this);

        }

        @Override
        public void onClick(@NonNull View view) {
            switch (view.getId()){
                case R.id.rr_advanced_qi:
                    Intent intent = null;
                        switch (getAdapterPosition()){
                            case 0:
                                intent = new Intent(context, QIAirwayRespiratoryActivity.class);
                                break;
                            case 1:
                                intent = new Intent(context, CardiovascularActivity.class);
                                break;
                            case 2:
                                intent = new Intent(context, NeurologicActivity.class);
                                break;
                            case 3:
                                intent = new Intent(context, RegionalActivity.class);
                                break;
                            case 4:
                                intent = new Intent(context, ProceduralActivity.class);
                                break;
                            case 5:
                                intent = new Intent(context, PharmacyBloodBank.class);
                                break;
                            case 6:
                                intent = new Intent(context, PatientSafety.class);
                                break;
                            case 7:
                                intent = new Intent(context, MorbidityMortality.class);
                                break;
                            case 8:
                                intent = new Intent(context, ComplianceActivity.class);
                                break;
                        }
                        if(intent!=null){
                            intent.putExtra(S.patient_details, patient_id);
                            intent.putExtra(S.save_qi_data,mSavedQI);
                            context.startActivity(intent);
                        }

                    break;
            }
        }
    }
    public AdvanceQIAdapter(List<AdvancedQIModal> moviesList, Context context, String patient_id, @NonNull ArrayList<AdvancedQIModal> save_qi) {
        this.moviesList = moviesList;
        this.context = context;
        this.patient_id = patient_id;
        this.mSavedQI.addAll(save_qi);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advanced_qi_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_notificationtext.setText(moviesList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}