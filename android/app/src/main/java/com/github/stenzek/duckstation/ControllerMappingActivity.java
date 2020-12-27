package com.github.stenzek.duckstation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ControllerMappingActivity extends AppCompatActivity {

    private static final int NUM_CONTROLLER_PORTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsCollectionFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Controller Mapping");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_controller_mapping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_load_profile) {
            return true;
        } else if (id == R.id.action_save_profile) {
            return true;
        } else if (id == R.id.action_clear_bindings) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private int controllerIndex;

        public SettingsFragment(int controllerIndex) {
            this.controllerIndex = controllerIndex;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            final SharedPreferences sp = getPreferenceManager().getSharedPreferences();
            String controllerType = sp.getString(String.format("Controller%d/Type", controllerIndex), "None");
            String[] controllerButtons = AndroidHostInterface.getControllerButtonNames(controllerType);
            String[] axisButtons = AndroidHostInterface.getControllerAxisNames(controllerType);

            final PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(getContext());
            if (controllerButtons != null) {
                for (String buttonName : controllerButtons) {
                    final ControllerBindingPreference cbbp = new ControllerBindingPreference(getContext(), null);
                    final String preferenceKey = String.format("Controller%d/Button%s", controllerIndex, buttonName);
                    final String currentBinding = sp.getString(preferenceKey, null);
                    cbbp.init(controllerIndex, buttonName, preferenceKey, currentBinding);
                    ps.addPreference(cbbp);
                }
            }
            if (axisButtons != null) {
                for (String axisName : axisButtons) {
                    // final ControllerButtonBindingPreference cbbp = new ControllerButtonBindingPreference(getContext(), null);
                    // cbbp.init(controllerIndex, buttonName);
                    // ps.addPreference(cbbp);
                }
            }

            setPreferenceScreen(ps);
        }
    }

    public static class SettingsCollectionFragment extends Fragment {
        private SettingsCollectionAdapter adapter;
        private ViewPager2 viewPager;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_settings_collection, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            adapter = new SettingsCollectionAdapter(this);
            viewPager = view.findViewById(R.id.view_pager);
            viewPager.setAdapter(adapter);

            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.setText(String.format("Port %d", position + 1))
            ).attach();
        }
    }

    public static class SettingsCollectionAdapter extends FragmentStateAdapter {
        public SettingsCollectionAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new SettingsFragment(position + 1);
        }

        @Override
        public int getItemCount() {
            return NUM_CONTROLLER_PORTS;
        }
    }
}