package org.bochman.upapp.presentation.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.bochman.upapp.R;
import org.bochman.upapp.data.repository.PoiRepository;
import org.bochman.upapp.databinding.ActivtySettingsBinding;
import org.bochman.upapp.presentation.masterDetail.POIMasterActivity;

import java.util.Objects;

/**
 * JetPack settings is replacing deprecated preferences.
 */
public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    /** JetPack ViewBinding */
    private ActivtySettingsBinding activtySettingsBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // JetPack ViewBinding based inflation
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        activtySettingsBinding = ActivtySettingsBinding.inflate(layoutInflater);
        setContentView(activtySettingsBinding.getRoot());



        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, androidx.preference.Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_container, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference DeleteFavouritesPreference= findPreference("delete_favourites");

            assert DeleteFavouritesPreference != null;
            DeleteFavouritesPreference.setOnPreferenceClickListener(preference -> {

                PoiRepository poiRepository=new PoiRepository(Objects.requireNonNull(getActivity()).getApplication());
                poiRepository.deleteAllFavourites();
                return true;
            });

            // do something with this preference
        }
    }

    /**
     * handle menu events.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //using the same menu as master activity.
        getMenuInflater().inflate(R.menu.places_list_activity_menu, menu);
        MenuItem preferencesItem = menu.findItem(R.id.action_preferences);
        return true;
    }

    /**
     * Menu click handler.
     *
     * Navigate back to parent activity.
     *
     * @param item - the menu item being accessed
     * @return if handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpTo(this, new Intent(this, POIMasterActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



}