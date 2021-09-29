package com.example.familymapclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private DataCache cache = DataCache.getInstance();
    private Map<String, ArrayList<Event>> people_events;

    private LinearLayout eventInformation;
    private ImageView gender;
    private TextView associatedPersonName;
    private TextView eventDetails;

    private static final String PERSON_ID = "personID";

    private ArrayList<Polyline> markerLifeStory;
    private ArrayList<Polyline> markerFamilyTree;
    private ArrayList<Polyline> markerSpouse;

    private ArrayList<Marker> markers;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.SearchActivityItem);
        item.setIcon(new IconDrawable(getActivity(), FontAwesomeIcons.fa_user).colorRes(R.color.white).actionBarSize());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            setHasOptionsMenu(true);
        }
        else if (getActivity() instanceof EventActivity) {
            setHasOptionsMenu(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        Intent intent;
        switch (menu.getItemId()) {
            case R.id.SearchActivityItem:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.SettingsActivityItem:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventInformation = view.findViewById(R.id.EventInformation);
        gender = view.findViewById(R.id.GenderIcon);
        associatedPersonName = view.findViewById(R.id.TopEventInfo);
        eventDetails = view.findViewById(R.id.BottomEventInfo);

        markerLifeStory = new ArrayList<>();
        markerFamilyTree = new ArrayList<>();
        markerSpouse = new ArrayList<>();

        markers = new ArrayList<>();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        people_events = cache.get_people_and_events();
        ArrayList<Event> events = new ArrayList<>(cache.getEventsAfterFilters());
        people_events = cache.filter(people_events, events);

        addMarkers(events);

        setUpDisplay();
    }

    @Override
    public void onMapLoaded() {}

    /* Useful Map Related Methods
     * GoogleMap.moveCamera(...):
     * GoogleMap.animateCamera(...):
     */

    private void addMarkers(List<Event> events) {
        Map<String, String> event_color = getEventColorMap(events);

        for (Event event : events) {
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
            String firstName = cache.getPerson(event.getPersonID()).getFirstName();
            String eventType = event.getEventType();
            String title = firstName + "'s " + eventType;
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(location)
                    .title(title)
                    .icon(getMarkerIcon(event_color.get(eventType.toUpperCase()))));
            marker.setTag(event);
            markers.add(marker);
        }

        map.setOnMarkerClickListener(this);
    }

    private Map<String, String> getEventColorMap(List<Event> events) {
        List<String> colors = new ArrayList<>();
        colors.add("#FF0000");
        colors.add("#FFA500");
        colors.add("#FFFF00");
        colors.add("#008000");
        colors.add("#0000FF");
        colors.add("#4B0082");
        colors.add("#EE82EE");
        colors.add("#DFFF00");
        colors.add("#9FE2BF");
        colors.add("#40E0D0");
        colors.add("#6495ED");
        colors.add("#9c7eb8");
        colors.add("#4c344c");
        colors.add("#2464b4");
        colors.add("#4c344c");

        int i = 0;
        Map<String, String> event_color = new HashMap<>();
        for (Event event : events) {
            String eventType = event.getEventType().toUpperCase();
            if (event_color.containsKey(eventType)) {
                continue;
            }
            else {
                event_color.put(eventType, colors.get(i));
                i++;
                if (i == colors.size()) {
                    break;
                }
            }
        }
        return event_color;
    }

    private BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Event event = (Event) marker.getTag();
        Person person = cache.getPerson(event.getPersonID());

        cache.setSelectedEvent(event);
        cache.setSelectedPerson(person);

        addLifeStoryLines(cache.hasLifeStoryLines(), person.getPersonID());
        addFamilyTreeLines(cache.hasFamilyTreeLines(), event.getEventID());
        addSpouseLine(cache.hasSpouseLine(), event.getEventID());

        setUpDisplay();

        eventInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra(PERSON_ID, person.getPersonID());
                startActivity(intent);
            }
        });

        return true;
    }

    private void addLifeStoryLines(boolean hasLifeStoryLines, String personID) {
        deleteLines(markerLifeStory);
        if (hasLifeStoryLines) {
            ArrayList<Event> events = people_events.get(personID);
            for (int i = 0; i < events.size() - 1; i++) {
                Event start_event = events.get(i);
                Event end_event = events.get(i + 1);
                LatLng start_loc = new LatLng(start_event.getLatitude(), start_event.getLongitude());
                LatLng end_loc = new LatLng(end_event.getLatitude(), end_event.getLongitude());
                markerLifeStory.add(map.addPolyline(new PolylineOptions()
                        .add(start_loc, end_loc)
                        .width(5)
                        .color(Color.RED)));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        map.clear();
    }

    private void deleteLines(ArrayList<Polyline> lines) {
        for (Polyline line : lines) {
            line.remove();
        }
        lines.clear();
    }

    private void addFamilyTreeLines(boolean hasFamilyTreeLines, String eventID) {
        deleteLines(markerFamilyTree);
        if (hasFamilyTreeLines) {
            int thickness = 16;
            addFamilyTreeLinesHelper(eventID, thickness);
        }
    }

    private void addFamilyTreeLinesHelper(String start_eventID, int thickness) {
        Event start_event = cache.getEvent(start_eventID);
        Person person = cache.getPerson(start_event.getPersonID());
        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();

        Event end_event;
        LatLng start_loc;
        LatLng end_loc;
        String end_eventID;
        if (people_events.containsKey(fatherID) && people_events.get(fatherID).size() > 0) {
            end_event = people_events.get(fatherID).get(0);
            start_loc = new LatLng(start_event.getLatitude(), start_event.getLongitude());
            end_loc = new LatLng(end_event.getLatitude(), end_event.getLongitude());
            markerFamilyTree.add(map.addPolyline(new PolylineOptions()
                    .add(start_loc, end_loc)
                    .width(thickness)
                    .color(Color.BLUE)));

            end_eventID = end_event.getEventID();
            addFamilyTreeLinesHelper(end_eventID, thickness / 2);
        }
        if (people_events.containsKey(motherID) && people_events.get(motherID).size() > 0) {
            end_event = people_events.get(motherID).get(0);
            start_loc = new LatLng(start_event.getLatitude(), start_event.getLongitude());
            end_loc = new LatLng(end_event.getLatitude(), end_event.getLongitude());
            markerFamilyTree.add(map.addPolyline(new PolylineOptions()
                    .add(start_loc, end_loc)
                    .width(thickness)
                    .color(Color.BLUE)));

            end_eventID = end_event.getEventID();
            addFamilyTreeLinesHelper(end_eventID, thickness / 2);
        }
    }

    private void addSpouseLine(boolean hasSpouseLine, String eventID) {
        deleteLines(markerSpouse);
        if (hasSpouseLine) {
            Event start_event = cache.getEvent(eventID);
            Person person = cache.getPerson(start_event.getPersonID());
            String spouseID = person.getSpouseID();

            if (people_events.containsKey(spouseID) && people_events.get(spouseID).size() > 0) {
                Event end_event = people_events.get(spouseID).get(0);
                LatLng start_loc = new LatLng(start_event.getLatitude(), start_event.getLongitude());
                LatLng end_loc = new LatLng(end_event.getLatitude(), end_event.getLongitude());
                markerSpouse.add(map.addPolyline(new PolylineOptions()
                        .add(start_loc, end_loc)
                        .width(5)
                        .color(Color.YELLOW)));
            }
        }
    }

    private void setUpDisplay() {
        Event event = null;
        Person person = null;
        if (getActivity() instanceof MainActivity) {
            event = cache.getSelectedEvent();
            person = cache.getSelectedPerson();
        }
        else if (getActivity() instanceof EventActivity) {
            event = cache.getDisplayingEvent();
            person = cache.getDisplayingPerson();

            addLifeStoryLines(cache.hasLifeStoryLines(), person.getPersonID());
            addFamilyTreeLines(cache.hasFamilyTreeLines(), event.getEventID());
            addSpouseLine(cache.hasSpouseLine(), event.getEventID());
        }
        if (event != null && person != null) {
            if (person.getGender().equals("f")) {
                gender.setImageResource(R.drawable.symbol_female);
            }
            else if (person.getGender().equals("m")) {
                gender.setImageResource(R.drawable.symbol_male);
            }

            String name = person.getFirstName() + " " + person.getLastName();
            associatedPersonName.setText(name);

            String details = event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
            eventDetails.setText(details);

            float latitude = event.getLatitude();
            float longitude = event.getLongitude();
            LatLng location = new LatLng(latitude, longitude);
            map.animateCamera(CameraUpdateFactory.newLatLng(location));
        }
    }
}