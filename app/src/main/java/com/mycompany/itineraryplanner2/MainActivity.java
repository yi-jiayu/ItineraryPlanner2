package com.mycompany.itineraryplanner2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements BudgetFragment.OnFragmentInteractionListener,
        AttractionsFragment.OnFragmentInteractionListener,
        ItineraryFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private BudgetFragment budgetFragment;
    private AttractionsFragment attractionsFragment;
    private ItineraryFragment itineraryFragment;

    ArrayList<String> checkedAttractions = new ArrayList<>();
    private int budget;
    private String hotel;
    private boolean itineraryExhaustiveEnumeration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        try (InputStream fileInputStream = getResources().openRawResource(R.raw.attractions)) {
            Routes.generateRoutes(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttractionsSelected(ArrayList<String> selectedAttractions) {
        Log.i("MainActivity", "Attraction selected");
        if (itineraryFragment != null) {
            itineraryFragment.updateItinerary(selectedAttractions);
        }
    }

    @Override
    public void onBudgetUpdated(int budget) {
        Log.i("MainActivity", "Budget updated: " + String.valueOf(budget));
        this.budget = budget;
        if (itineraryFragment != null) {
            itineraryFragment.updateItinerary(budget);
        }
    }

    @Override
    public void onHotelUpdated(String hotel) {
        Log.i("MainActivity", "Hotel updated to " + hotel);
        this.hotel = hotel;
        if (attractionsFragment != null) {
            attractionsFragment.updateHotel(hotel);
        }
        if (itineraryFragment != null) {
            itineraryFragment.updateItinerary(hotel);
        }

    }

    @Override
    public void onSearchModeUpdated(boolean exhaustiveMode) {
        Log.i("MainActivity", "Exhaustive mode set to " + String.valueOf(exhaustiveMode));
        this.itineraryExhaustiveEnumeration = exhaustiveMode;
        if (itineraryFragment != null) {
            itineraryFragment.updateItinerary(exhaustiveMode);
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final FragmentManager mFragmentManager;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return BudgetFragment.newInstance();
                case 1:
                    return AttractionsFragment.newInstance();
                case 2:
                    return ItineraryFragment.newInstance(budget, hotel, itineraryExhaustiveEnumeration);
                default:
                    // Return a PlaceholderFragment (defined as a static inner class below).
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    budgetFragment = (BudgetFragment) createdFragment;
                    Log.i("instantiateItem", "Created BudgetFragment");
                    break;
                case 1:
                    attractionsFragment = (AttractionsFragment) createdFragment;
                    Log.i("instantiateItem", "Created AttractionsFragment");
                    break;
                case 2:
                    itineraryFragment = (ItineraryFragment) createdFragment;
                    Log.i("instantiateItem", "Created ItineraryFragment");
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BUDGET";
                case 1:
                    return "ATTRACTIONS";
                case 2:
                    return "ITINERARY";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
