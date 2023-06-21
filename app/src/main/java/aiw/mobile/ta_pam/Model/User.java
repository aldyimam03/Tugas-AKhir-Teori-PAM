package aiw.mobile.ta_pam.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userId;
    private String fullname;
    private String username;
    private String email;
    private String profilePicture;

    public User() {
        // Diperlukan konstruktor kosong untuk deserialisasi dari Firebase Realtime Database
    }

    public User(String userId, String fullname, String username, String email, String profilePicture) {
        this.userId = userId;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public User(String fullname, String username){
        this.fullname = fullname;
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserId() {
        return userId;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Implementasi Parcelable
    protected User(Parcel in) {
        userId = in.readString();
        fullname = in.readString();
        username = in.readString();
        email = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(fullname);
        dest.writeString(username);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
