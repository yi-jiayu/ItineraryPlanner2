package com.mycompany.itineraryplanner2.itineraryplanner;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.mycompany.itineraryplanner2.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class AttractionsFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    private String previousHotel;
    private ArrayList<String> attractions;
    private HashSet<String> checkedAttractions;
    private ArrayAdapter<String> attractionsAdapter;

    public static AttractionsFragment newInstance() {
        AttractionsFragment fragment = new AttractionsFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AttractionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listLayout = inflater.inflate(android.R.layout.list_content, container, false);
        ListView listView = (ListView) listLayout.findViewById(android.R.id.list);
//        listView.setPadding(0, 8, 0, 0);
//        ((FrameLayout) listView.getParent()).setPadding(0, 8, 0, 0);
        listView.setSelector(android.R.color.transparent);
//        listView.setDivider(null);
//        listView.setDividerHeight(0);
        return listLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attractions = new ArrayList<>(Data.attractions);
        checkedAttractions = new HashSet<>();

        attractionsAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.attraction, R.id.text1, attractions);
        setListAdapter(attractionsAdapter);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("AttractionsFragment", "List item click");

        if (null != mListener) {
            String attraction = attractionsAdapter.getItem(position);
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            if (!checkBox.isChecked()) {
                checkedAttractions.add(attraction);
            } else {
                checkedAttractions.remove(attraction);
            }
            checkBox.toggle();

            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onAttractionsSelected(new ArrayList<>(checkedAttractions));
        }
    }

    public void updateHotel(String hotel) {
        Log.i("AttractionsFragment", "Updating hotel");
        if (previousHotel != null) {
            attractionsAdapter.add(previousHotel);
        }
        attractionsAdapter.remove(hotel);
        attractionsAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onAttractionsSelected(ArrayList<String> selectedAttractions);
    }

}
