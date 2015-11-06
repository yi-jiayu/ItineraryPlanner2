package com.mycompany.itineraryplanner2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ItineraryFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    int budget;
    boolean exhaustiveMode;

    // TODO: Rename and change types of parameters
    public static ItineraryFragment newInstance(int budget, ArrayList<String> toVisit, boolean exhaustiveMode) {
        ItineraryFragment fragment = new ItineraryFragment();
        Bundle args = new Bundle();
        args.putInt("budget", budget);
        args.putStringArrayList("toVisit", toVisit);
        args.putBoolean("exhaustiveMode", exhaustiveMode);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItineraryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            budget = getArguments().getInt("budget");
            Log.i("budget", String.valueOf(budget));
            exhaustiveMode = getArguments().getBoolean("exhaustiveMode");
            ArrayList<String> attractionsToVisit = getArguments().getStringArrayList("toVisit");
            Log.i("attractions to visit", attractionsToVisit.toString());
            if (!attractionsToVisit.isEmpty()) {
                ArrayList<String> itinerary = new OptimalRouteFinder(budget, "Resorts World Sentosa", attractionsToVisit).findOptimalRoute();
                Log.i("OptimalRoute", itinerary.toString());
            }
//            if (!attractionsToVisit.isEmpty()) {
//                itinerary = new OptimalRouteFinder(budget, "Resorts World Sentosa", attractionsToVisit).findOptimalRoute();
//            } else {
//                itinerary = attractionsToVisit;
//            }
            setListAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, attractionsToVisit));
        }

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
        super.onListItemClick(l, v, position, id);

//        if (null != mListener) {
//            // Notify the active callbacks interface (the activity, if the
//            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
//        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    class OptimalRouteFinder {
        class CandidateSolution {
            ArrayList<String> pathTaken = new ArrayList<>();
            int total_time = Integer.MAX_VALUE;
            int total_cost = Integer.MAX_VALUE;
        }

        int budget;
        String hotel;
        ArrayList<String> destinations;
        CandidateSolution candidateSolution;

        public OptimalRouteFinder(int budget, String hotel, ArrayList<String> destinations) {
            this.budget = budget;
            this.hotel = hotel;
            this.destinations = destinations;
            candidateSolution = new CandidateSolution();
        }

        ArrayList<String> findOptimalRoute() {
            tracePath(hotel, 0, 0, new ArrayList<>(destinations), new ArrayList<String>());
            return candidateSolution.pathTaken;
        }

        void tracePath(String currentLocation, int currentTiming, int currentCost,
                       ArrayList<String> remainingDestinations,
                       ArrayList<String> pathTaken) {

            Log.i("candidatesln", String.valueOf(this.candidateSolution.total_time));
            Log.i("currenttiming", String.valueOf(currentTiming));
//            if (currentCost > budget || currentTiming > this.candidateSolution.total_time) {
//                return;
//            }

            Log.i("remainingDestinations", String.valueOf(remainingDestinations.isEmpty()));
            if (remainingDestinations.isEmpty()) {
                Log.i("HELO", "hi");
                for (TransportMode transportMode : TransportMode.values()) {
                    Log.i("transportMode", transportMode.toString());
                    int finalCost = currentCost += Routes.getCost(transportMode, currentLocation, hotel);
                    if (finalCost <= budget) {
                        int finalTiming = currentTiming += Routes.getTiming(transportMode, currentLocation, hotel);
                        if (finalTiming < candidateSolution.total_time) {
                            candidateSolution.total_cost = finalCost;
                            candidateSolution.total_time = finalTiming;
                            pathTaken.add(hotel);
                            candidateSolution.pathTaken = pathTaken;
                            Log.i("FinalPathTaken", candidateSolution.pathTaken.toString());
                        }
                    }
                }
            } else {
                for (TransportMode transportMode : TransportMode.values()) {
                    for (String destination : remainingDestinations) {
                        ArrayList<String> r = new ArrayList<>(remainingDestinations);
                        r.remove(destination);
                        ArrayList<String> p = new ArrayList<>(pathTaken);
                        p.add(destination);
                        tracePath(destination,
                                currentTiming += Routes.getTiming(transportMode, currentLocation, destination),
                                currentCost += Routes.getCost(transportMode, currentLocation, destination),
                                r,
                                p);
                    }
                }
            }
        }
    }


}
