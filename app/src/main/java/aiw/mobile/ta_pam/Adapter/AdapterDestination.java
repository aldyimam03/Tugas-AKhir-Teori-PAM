package aiw.mobile.ta_pam.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.UI.DetailDestinationPage;
import aiw.mobile.ta_pam.UI.EditPage;
import aiw.mobile.ta_pam.UI.HomePage;
import aiw.mobile.ta_pam.databinding.ItemViewBinding;

public class AdapterDestination extends RecyclerView.Adapter<AdapterDestination.ViewHolder> {

    private final ArrayList<Destination> listDestination;

    public AdapterDestination(ArrayList<Destination> listDestination) {
        this.listDestination = listDestination;
    }

    @NonNull
    @Override
    public AdapterDestination.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewBinding binding = ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDestination.ViewHolder holder, int position) {
        holder.bind(listDestination.get(position));
    }

    @Override
    public int getItemCount() {
        return listDestination.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Activity activity;
        final ItemViewBinding binding;
        FirebaseAuth mAuth;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        public ViewHolder(ItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.activity = (Activity) binding.getRoot().getContext();

            mAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance("https://uap-pam-1-default-rtdb.asia-southeast1.firebasedatabase.app/");
            databaseReference = firebaseDatabase.getReference();
        }

        public void bind(Destination destination){
            binding.tvTitleDestinasi.setText(destination.getNama());
            binding.ivPensil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EditPage.class);
                    intent.putExtra("EXTRA DESTINATION", destination);
                    v.getContext().startActivity(intent);
                }
            });
            binding.ivSampah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.child("destination").child(mAuth.getUid()).child(destination.getKey()).removeValue();
                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setMessage("Apakah anda ingin menghapus destinasi " + destination.getNama() + "?");
                    builder.show();
                }
            });
            binding.cvDetailDestination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailDestinationPage.class);
                    intent.putExtra("EXTRA DESTINATION", destination);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
