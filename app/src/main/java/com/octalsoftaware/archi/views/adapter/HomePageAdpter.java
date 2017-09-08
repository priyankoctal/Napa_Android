package com.octalsoftaware.archi.views.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.Gson;
import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.lib.swipe.SwipeLayout;
import com.octalsoftaware.archi.lib.swipe.adapters.RecyclerSwipeAdapter;
import com.octalsoftaware.archi.models.HomePageModal;
import com.octalsoftaware.archi.views.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.octalsoftaware.archi.utils.constants.I.ALERT_REOPEN;
import static com.octalsoftaware.archi.utils.constants.I.CANCEL_CASE;
import static com.octalsoftaware.archi.utils.constants.I.CHARGE;
import static com.octalsoftaware.archi.utils.constants.I.IMAGES;
import static com.octalsoftaware.archi.utils.constants.I.PATIENT_DETAIS;
import static com.octalsoftaware.archi.utils.constants.I.QI;

/**
 * Created by anandj on 4/19/2017.
 */

public class HomePageAdpter extends RecyclerSwipeAdapter<HomePageAdpter.MyViewHolder> {

    private List<HomePageModal> moviesList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    @Nullable
    private HomeActivity context = null;
    @NonNull
    private List<HomePageModal> arrayList= new ArrayList<>();


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_notificationtext, txt_canceled;
        SwipeLayout swipeLayout;
        LinearLayout ll_charge, ll_qi, ll_image;
        LinearLayout ll_cancel, ll_reason;
        ImageView imgcharge,imgqi,img_camera;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txt_notificationtext = (TextView) view.findViewById(R.id.txt_settingname);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            ll_charge = (LinearLayout) view.findViewById(R.id.ll_charge);
            ll_qi = (LinearLayout) view.findViewById(R.id.ll_qi);
            ll_image = (LinearLayout) view.findViewById(R.id.ll_image);
            ll_cancel = (LinearLayout) view.findViewById(R.id.ll_cancel);
            ll_reason = (LinearLayout) view.findViewById(R.id.ll_reason);
            txt_canceled = (TextView) view.findViewById(R.id.txt_canceled);
            imgcharge = (ImageView)view.findViewById(R.id.imgcharge);
            imgqi = (ImageView)view.findViewById(R.id.imgqi);
            img_camera = (ImageView)view.findViewById(R.id.img_camera);
        }
    }

    public HomePageAdpter(@NonNull List<HomePageModal> moviesList, HomeActivity context) {
        this.moviesList = moviesList;
        this.context = context;
        arrayList.addAll(moviesList);
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_page_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final String patient_id = moviesList.get(position).getId();
        final String patient_name = moviesList.get(position).getName();
        final HomePageModal homePageModal = moviesList.get(position);
        final int pos = position;

        if(moviesList.get(position).getIs_charged().equals("0"))
            holder.imgcharge.setVisibility(View.GONE);
        else
            holder.imgcharge.setVisibility(View.VISIBLE);

        if(moviesList.get(position).getIs_qi().equals("0"))
            holder.imgqi.setVisibility(View.GONE);
        else
            holder.imgqi.setVisibility(View.VISIBLE);

        if(moviesList.get(position).getTotalImages().equals("0"))
            holder.img_camera.setVisibility(View.GONE);
        else
            holder.img_camera.setVisibility(View.VISIBLE);



        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.swipeLayout.clearDragEdge();

        // Drag From Left
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // On Click on Row
        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  HomePageModal object = homePageModal
                Gson gson = new Gson();
                String jsonString = gson.toJson(homePageModal);
                context.startNewActivity(PATIENT_DETAIS,jsonString,pos,patient_name);
            }
        });
        // click on image
        holder.ll_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  holder.swipeLayout.close();
                context.startNewActivity(IMAGES, patient_id,pos,patient_name);
            }
        });

        // click on qi
        holder.ll_qi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.close();
                context.startNewActivity(QI, patient_id,pos,patient_name);
            }
        });

        // click on charge
        holder.ll_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.close();
                context.startNewActivity(CHARGE, patient_id,pos,patient_name);
            }
        });

        //  click on cancel
        holder.ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.close();
                context.startNewActivity(CANCEL_CASE, patient_id,pos,patient_name);
            }
        });

        //  click on reopen
        holder.ll_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startNewActivity(ALERT_REOPEN, patient_id,pos,patient_name);
                holder.swipeLayout.close();
            }
        });

        holder.txt_notificationtext.setText(moviesList.get(position).getName());


        switch (Integer.parseInt(moviesList.get(position).getStatus())) {
            case 6:
                // visible reopen tab becoz status is cancel
                holder.ll_reason.setVisibility(View.VISIBLE);
                holder.txt_canceled.setVisibility(View.VISIBLE);

                holder.imgcharge.setVisibility(View.GONE);
                holder.imgqi.setVisibility(View.GONE);
                holder.img_camera.setVisibility(View.GONE);

                // hide cancel tab becoz status is cancel
                holder.ll_cancel.setVisibility(View.GONE);

                // stop right drag
                holder.swipeLayout.setDrag(SwipeLayout.DragEdge.Left, holder.swipeLayout.findViewById(R.id.bottom_wrapper1));
                break;
            case 1:
                // hide reopen tab untill status is npt reach cancel
                holder.ll_reason.setVisibility(View.GONE);
                holder.txt_canceled.setVisibility(View.GONE);
                holder.ll_cancel.setVisibility(View.VISIBLE);
                break;
            default:
                holder.ll_reason.setVisibility(View.GONE);
                holder.txt_canceled.setVisibility(View.GONE);
                break;

        }
        // viewBinderHelper.bind(holder.swipeLayout, moviesList.get(position).getId());
        // holder.txt_notificationtext.setText(moviesList.get(position).getName());
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
            for (HomePageModal wp : arrayList) {
                if (wp.getLocation_name().toLowerCase(Locale.getDefault())
                        .contains(charText) ) {
                    moviesList.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }
}
