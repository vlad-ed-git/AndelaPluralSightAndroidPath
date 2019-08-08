package com.dev.vlad.travelmantics.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.vlad.travelmantics.R;
import com.dev.vlad.travelmantics.TravelDealActivity;
import com.dev.vlad.travelmantics.Utils.FirebaseUtil;
import com.dev.vlad.travelmantics.Utils.TravelDeals;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class TravelDealsAdapter extends RecyclerView.Adapter<TravelDealsAdapter.CustomViewHolder> {

    private  ArrayList<TravelDeals> travelDealsArrayList;

    public TravelDealsAdapter(){
        travelDealsArrayList = FirebaseUtil.travelDealsArrayList;
        CollectionReference newDealRef = FirebaseUtil.travelDealsRef;
        newDealRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("Travel Deals Adapter", e.getMessage());
                } else {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    travelDealsArrayList.add(dc.getDocument().toObject(TravelDeals.class));
                                    notifyItemInserted(travelDealsArrayList.size() - 1);
                                    break;
                                case MODIFIED:
                                    //todo
                                    break;
                                case REMOVED:
                                    //todo
                                    break;
                            }
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
       Context context =  parent.getContext();
        View itemView = LayoutInflater.from(context).inflate( R.layout.travel_deal_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        TravelDeals travelDeal = travelDealsArrayList.get(position);
        holder.bindData(travelDeal);
    }

    @Override
    public int getItemCount() {
        return travelDealsArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView travel_deal_title_tv, travel_deal_desc_tv, travel_deal_price_tv;
        private ImageView travel_deal_iv;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            travel_deal_title_tv = itemView.findViewById(R.id.travel_deal_title_tv);
            travel_deal_desc_tv = itemView.findViewById(R.id.travel_deal_desc_tv);
            travel_deal_price_tv = itemView.findViewById(R.id.travel_deal_price_tv);
            travel_deal_iv = itemView.findViewById(R.id.travel_deal_iv);
            itemView.setOnClickListener(this);
        }

        void bindData(final TravelDeals travelDeal) {
            travel_deal_title_tv.setText(travelDeal.getTitle());
            travel_deal_desc_tv.setText(travelDeal.getDescription());
            travel_deal_price_tv.setText(travelDeal.getPrice());
            if(travelDeal.getImageUri() != null) {
                Uri img_uri = Uri.parse(travelDeal.getImageUri());
                Picasso.get().load(img_uri).into(travel_deal_iv);
            }
        }

        @Override
        public void onClick(View v) {
           int clickedPosition =  getAdapterPosition();
           TravelDeals clickedDeal = travelDealsArrayList.get(clickedPosition);
           v.getContext().startActivity(new Intent(v.getContext(), TravelDealActivity.class).putExtra(TravelDealActivity.PASSED_DEAL, clickedDeal));
        }
    }
}
