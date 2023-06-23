package aiw.mobile.ta_pam.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.UI.DetailDestinationPage;
import aiw.mobile.ta_pam.UI.EditPage;
import aiw.mobile.ta_pam.UI.HomePage;
import aiw.mobile.ta_pam.databinding.ItemViewBinding;

public class AdapterDestination extends RecyclerView.Adapter<AdapterDestination.ViewHolder> implements Filterable {

    private final ArrayList<Destination> listDestination;
    private ArrayList<Destination> filteredList;
    private final Context context;

    public AdapterDestination(ArrayList<Destination> listDestination, Context context) {
        this.listDestination = listDestination;
        this.context = context;
        filteredList = listDestination;
    }

    @NonNull
    @Override
    public AdapterDestination.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewBinding binding = ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDestination.ViewHolder holder, int position) {
        holder.bind(filteredList.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            //melakukan proses filter
            @Override
            protected FilterResults performFiltering(CharSequence search) {
                FilterResults results = new FilterResults();
                if (search == null || search.length() == 0) {
                    results.count = filteredList.size();
                    results.values = filteredList;
                } else {
                    String keyword = search.toString().toLowerCase();
                    filteredList = new ArrayList<>();
                    for (Destination d: listDestination) {
                        if (d.getNama().toLowerCase().contains(keyword)) {
                            filteredList.add(d);
                        }
                    }
                    results.count = filteredList.size();
                    results.values = filteredList;
                }
                return results;
            }

            //menampilkan proses filter
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<Destination>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

        public void bind(Destination destination) {
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
            Glide.with(context)
                    .load(destination.getImage())
                    .into(binding.ivGambarDestinasi);
        }
    }
}