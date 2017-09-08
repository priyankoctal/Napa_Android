package com.octalsoftaware.archi.views.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.octalsoftaware.archi.MyApplication;
import com.octalsoftaware.archi.R;
import com.octalsoftaware.archi.data.remote.retrofit.MyAPIService;
import com.octalsoftaware.archi.models.ProceduresModal;
import com.octalsoftaware.archi.utils.ConvertJsonToMap;
import com.octalsoftaware.archi.utils.Util;
import com.octalsoftaware.archi.utils.constants.S;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anandj on 5/9/2017.
 */

public class BookAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private String patient_id = "";
    @NonNull
    private List<ProceduresModal> resultList = new ArrayList<ProceduresModal>();
    private static final String TAG = BookAutoCompleteAdapter.class.getSimpleName();

    public BookAutoCompleteAdapter(Context context, String patient_id) {
        mContext = context;
        this.patient_id = patient_id;
        //   resultList.addAll(arrayList);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public ProceduresModal getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.auto_compleate, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.tv_country)).setText(getItem(position).getCode());
        return convertView;
    }

    @Nullable
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @NonNull
            @Override
            protected FilterResults performFiltering(@Nullable CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    List<ProceduresModal> books = findCcde(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = books;
                    filterResults.count = books.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, @Nullable FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<ProceduresModal>) results.values;
                       notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    /**
     * Returns a search result for the given code title.
     */
    @NonNull
    private List<ProceduresModal> findCcde(Context context, String bookTitle) {
        final List<ProceduresModal> procedure_detail = new ArrayList<>();
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(S.api_record_id, patient_id);
            jsonObject.put(S.api_procedure_codes, bookTitle);
            MyAPIService myApiEndpointInterface = MyApplication.getInstance().getRequestQueue().create(MyAPIService.class);
            Call<ResponseBody> call = myApiEndpointInterface.proceduresListAdapter(ConvertJsonToMap.toMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObject1 = new JSONObject(Util.convertRetrofitResponce(response)).getJSONObject(S.response);
                        if (jsonObject1.getBoolean(S.status)) {
                            JSONObject jsonObject2 = jsonObject1.getJSONArray(S.data).getJSONObject(0);
                            JSONArray jsonArray = jsonObject2.getJSONArray(S.api_procedure_code);
                            if (jsonArray.length() != 0) {
                                JSONObject jsonObject5 = jsonArray.getJSONObject(0);//Util.getProcedures(jsonObject2);
                                if (jsonObject5 != null) {
                                    Iterator<String> keys1 = jsonObject5.keys();
                                    resultList.clear();
                                    while (keys1.hasNext()) {
                                        String key = keys1.next();
                                        ProceduresModal proceduresModal = new ProceduresModal();
                                        proceduresModal.setCode(jsonObject5.getString(key));
                                        resultList.add(proceduresModal);
                                    }
                                    notifyDataSetChanged();
                                }
                            }
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return procedure_detail;
    }
}