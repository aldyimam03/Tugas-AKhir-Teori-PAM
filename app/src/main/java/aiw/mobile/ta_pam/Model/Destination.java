package aiw.mobile.ta_pam.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Destination implements Parcelable {
    private String nama;
    private String deskripsi;
    private String lokasi;
    private String key;

    public Destination() {
        // Diperlukan konstruktor tanpa argumen kosong untuk Firebase Realtime Database.
    }

    public Destination(String nama, String lokasi, String deskripsi) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.nama);
        dest.writeString(this.lokasi);
        dest.writeString(this.deskripsi);
    }
    private Destination(Parcel in){
        this.key = in.readString();
        this.nama = in.readString();
        this.lokasi = in.readString();
        this.deskripsi = in.readString();
    }

    public static final Creator<Destination> CREATOR = new Creator<Destination>(){

        @Override
        public Destination createFromParcel(Parcel parcel) {
            return new Destination(parcel);
        }

        @Override
        public Destination[] newArray(int i) {
            return new Destination[i];
        }
    };
}
