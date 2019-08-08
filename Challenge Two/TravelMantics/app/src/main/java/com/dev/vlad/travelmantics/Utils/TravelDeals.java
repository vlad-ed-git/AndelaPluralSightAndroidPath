package com.dev.vlad.travelmantics.Utils;

import android.os.Parcel;
import android.os.Parcelable;

public class TravelDeals implements Parcelable {

    private String id;
    private String title;
    private String description;
    private String price;
    private String imageUri;

    public TravelDeals(){}

    public TravelDeals(String id, String title, String description, String price, String imageUri) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
    }


    protected TravelDeals(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        price = in.readString();
        imageUri = in.readString();
    }

    public static final Creator<TravelDeals> CREATOR = new Creator<TravelDeals>() {
        @Override
        public TravelDeals createFromParcel(Parcel in) {
            return new TravelDeals(in);
        }

        @Override
        public TravelDeals[] newArray(int size) {
            return new TravelDeals[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeString(imageUri);
    }
}
