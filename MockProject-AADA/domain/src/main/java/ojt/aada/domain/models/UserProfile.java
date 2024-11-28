package ojt.aada.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserProfile implements Parcelable {
    private String id;
    private String name;
    private String email;
    private String avatar;
    private String gender;
    private String dob;

    public UserProfile() {
    }

    public UserProfile(String id, String name, String email, String avatar, String gender, String dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.gender = gender;
        this.dob = dob;
    }

    protected UserProfile(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        avatar = in.readString();
        gender = in.readString();
        dob = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(avatar);
        dest.writeString(gender);
        dest.writeString(dob);
    }
}
