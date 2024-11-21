package ojt.aada.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CastnCrew implements Parcelable {
    private int id;
    @SerializedName("original_name")
    private String actorName;
    @SerializedName("profile_path")
    private String img;

    public CastnCrew(String actorName, String img) {
        this.actorName = actorName;
        this.img = img;
    }

    protected CastnCrew(Parcel in) {
        id = in.readInt();
        actorName = in.readString();
        img = in.readString();
    }

    public static final Creator<CastnCrew> CREATOR = new Creator<CastnCrew>() {
        @Override
        public CastnCrew createFromParcel(Parcel in) {
            return new CastnCrew(in);
        }

        @Override
        public CastnCrew[] newArray(int size) {
            return new CastnCrew[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(actorName);
        dest.writeString(img);
    }
}
