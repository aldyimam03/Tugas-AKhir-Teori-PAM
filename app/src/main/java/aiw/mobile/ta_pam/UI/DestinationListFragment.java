package aiw.mobile.ta_pam.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import aiw.mobile.ta_pam.Adapter.AdapterDestination;
import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.R;


public class DestinationListFragment extends Fragment {
    private View layout;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference destiantion;
    private AdapterDestination adapterDestination;

    RecyclerView rvDestination;
    private ArrayList<Destination> destinationArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.layout = inflater.inflate(R.layout.fragment_destination_list, null, false);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        destiantion = this.databaseReference.child("destination");

        rvDestination = this.layout.findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.layout.getContext());
        rvDestination.setLayoutManager(layoutManager);

        getAllData();

        return this.layout;
    }

    private void getAllData(){
        this.destiantion.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                destinationArrayList = new ArrayList<>();
                for (DataSnapshot s: snapshot.getChildren()){
                    Destination d = s.getValue(Destination.class);
                    System.out.println(d.getNama());
                    d.setKey(s.getKey());
                    destinationArrayList.add(d);
                }
                adapterDestination = new AdapterDestination(destinationArrayList);
                rvDestination.setAdapter(adapterDestination);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error");
            }
        });
    }
}