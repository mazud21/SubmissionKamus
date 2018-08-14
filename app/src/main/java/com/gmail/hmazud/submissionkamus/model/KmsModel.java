package com.gmail.hmazud.submissionkamus.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KmsModel implements Parcelable {

    private int id;
    private String word;
    private String translate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.word);
        dest.writeString(this.translate);
    }

    public KmsModel() {
    }

    public KmsModel(String word, String translate) {
        this.word = word;
        this.translate = translate;
    }

    protected KmsModel(Parcel in) {
        this.id = in.readInt();
        this.word = in.readString();
        this.translate = in.readString();
    }

    public static final Parcelable.Creator<KmsModel> CREATOR = new Parcelable.Creator<KmsModel>() {
        @Override
        public KmsModel createFromParcel(Parcel source) {
            return new KmsModel(source);
        }

        @Override
        public KmsModel[] newArray(int size) {
            return new KmsModel[size];
        }
    };
}
