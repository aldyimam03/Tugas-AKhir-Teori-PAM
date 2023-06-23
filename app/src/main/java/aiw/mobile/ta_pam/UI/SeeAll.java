package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;

import aiw.mobile.ta_pam.Adapter.AdapterDestination;
import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.R;

public class SeeAll extends AppCompatActivity{

    EditText searchBar;
    ImageView ivBackToHomePage;
    private FragmentManager fm;
    private DestinationListFragment destinationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        searchBar = findViewById(R.id.searchBar);
        ivBackToHomePage = findViewById(R.id.ivBackToHomePage);
        this.fm = getSupportFragmentManager();
        this.destinationListFragment = new DestinationListFragment();

        ivBackToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SeeAll.this, HomePage.class);
                startActivity(i);
            }
        });

        fm.beginTransaction()
                .add(R.id.frameRecyclerView, destinationListFragment,"FDestination")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DestinationListFragment dlf = (DestinationListFragment) getSupportFragmentManager().findFragmentByTag("FDestination");
        dlf.getAllData();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                DestinationListFragment dlf = (DestinationListFragment) getSupportFragmentManager().findFragmentByTag("FDestination");
                dlf.search(editable.toString());
            }
        });
    }
}