package com.octalsoftaware.archi.views.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.models.ImagesListModal;
import com.octalsoftaware.archi.views.activity.images.ImagesListActivity;

import java.util.List;

import static com.octalsoftaware.archi.utils.Util.confirmDialog;

/**
 * Created by anandj on 4/19/2017.
 */

public class ImagesListAdapter extends RecyclerView.Adapter<ImagesListAdapter.MyViewHolder> {

    private List<ImagesListModal> moviesList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    @Nullable
    private ImagesListActivity context = null;
    private DisplayImageOptions options;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txt_patient_name;
        private SwipeRevealLayout swipeLayout;
        private LinearLayout ll_delete;
        private ImageView img_patientimage;
        private FrameLayout delete_layout;
        public MyViewHolder(@NonNull View view) {
            super(view);
            txt_patient_name = (TextView) view.findViewById(R.id.txt_patient_name);
            swipeLayout = (SwipeRevealLayout)view.findViewById(R.id.swipe_layout);
            ll_delete = (LinearLayout)view.findViewById(R.id.ll_delete);
            img_patientimage = (ImageView)view.findViewById(R.id.img_patientimage);
            delete_layout = (FrameLayout)view.findViewById(R.id.delete_layout);

            delete_layout.setOnClickListener(this);

           /* ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog(context,context.getString(R.string.delete_confirm_msg),context.getString(android.R.string.yes),context.getString(android.R.string.cancel),"",0);
                }
            });*/

        }

        @Override
        public void onClick(@NonNull View view) {
            switch (view.getId()){
                case R.id.delete_layout:
                    confirmDialog(context,context.getString(R.string.delete_confirm_msg),context.getString(android.R.string.yes),context.getString(android.R.string.cancel),moviesList.get(getAdapterPosition()).getId(),0);

              //      context.deletePatientImage(moviesList.get(getAdapterPosition()).getId());
                    break;
            }
        }
    }
    public ImagesListAdapter(List<ImagesListModal> moviesList, @NonNull ImagesListActivity context) {
        this.moviesList = moviesList;
        this.context =  context;
         viewBinderHelper.setOpenOnlyOne(true);
        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.deafull_img);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        viewBinderHelper.bind(holder.swipeLayout, moviesList.get(position).getId());
        if(!moviesList.get(position).getUrl().equals(""))
            ImageLoader.getInstance().displayImage(moviesList.get(position).getUrl(),  holder.img_patientimage, options);

        String name = "";
        if(!moviesList.get(position).getExtension().equals(""))
                name = moviesList.get(position).getImageName().replace("."+moviesList.get(position).getExtension(),"");

        holder.txt_patient_name.setText(name);

       // holder.txt_notificationtext.setText(moviesList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
