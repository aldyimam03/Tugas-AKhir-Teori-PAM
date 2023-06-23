package aiw.mobile.ta_pam.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import aiw.mobile.ta_pam.Adapter.AdapterDestination;
import aiw.mobile.ta_pam.Model.Destination;
import aiw.mobile.ta_pam.R;

public class SeeAll extends AppCompatActivity{

    EditText searchBar;
    private FragmentManager fm;
    private DestinationListFragment destinationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        searchBar = findViewById(R.id.searchBar);
        this.fm = getSupportFragmentManager();
        this.destinationListFragment = new DestinationListFragment();

        fm.beginTransaction()
                .add(R.id.frameRecyclerView, destinationListFragment,"FDestination")
                .commit();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                DestinationListFragment dlf = (DestinationListFragment) getSupportFragmentManager().findFragmentByTag("FDestination");
                dlf.Search(editable.toString());
            }
        });
    }
}