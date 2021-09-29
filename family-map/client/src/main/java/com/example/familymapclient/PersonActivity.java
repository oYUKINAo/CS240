package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    private static final String PERSON_ID = "personID";
    private static final String EVENT_ID = "eventID";

    private static Person person;

    private static Person father = null;
    private static Person mother = null;
    private static Person spouse = null;
    private static Person child = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        ExpandableListView view = findViewById(R.id.ExpandableList);

        DataCache cache = DataCache.getInstance();
        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSON_ID);
        person = cache.getPerson(personID);

        TextView firstNameTextView = findViewById(R.id.FirstNameTextView);
        firstNameTextView.setText(person.getFirstName());

        TextView lastNameTextView = findViewById(R.id.LastNameTextView);
        lastNameTextView.setText(person.getLastName());

        TextView genderTextView = findViewById(R.id.GenderTextView);
        String gender = person.getGender();
        if (gender.equals("f")) {
            genderTextView.setText(R.string.Female);
        }
        else if (gender.equals("m")) {
            genderTextView.setText(R.string.Male);
        }

        Map<String, ArrayList<Event>> people_events = cache.get_people_and_events();
        people_events = cache.filter(people_events, cache.getEventsAfterFilters());
        ArrayList<Event> events = people_events.get(person.getPersonID());
        father = cache.getPerson(person.getFatherID());
        mother = cache.getPerson(person.getMotherID());
        spouse = cache.getPerson(person.getSpouseID());
        child = cache.getChild(person.getPersonID());

        List<Person> immediate_family = new ArrayList<>();
        if (father != null) {
            immediate_family.add(father);
        }
        if (mother != null) {
            immediate_family.add(mother);
        }
        if (spouse != null) {
            immediate_family.add(spouse);
        }
        if (child != null) {
            immediate_family.add(child);
        }

        view.setAdapter(new ExpandableListAdapter(events, immediate_family));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_EXPANDABLE_LIST_VIEW_TYPE = 0;
        private static final int PERSON_EXPANDABLE_LIST_VIEW_TYPE = 1;

        private final List<Event> events;
        private final List<Person> immediate_family;

        ExpandableListAdapter(List<Event> events, List<Person> immediate_family) {
            this.events = events;
            this.immediate_family = immediate_family;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_EXPANDABLE_LIST_VIEW_TYPE:
                    return events.size();
                case PERSON_EXPANDABLE_LIST_VIEW_TYPE:
                    return immediate_family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_EXPANDABLE_LIST_VIEW_TYPE:
                    return getString(R.string.EventExpandableGroupTitle);
                case PERSON_EXPANDABLE_LIST_VIEW_TYPE:
                    return getString(R.string.PersonExpandableGroupTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_EXPANDABLE_LIST_VIEW_TYPE:
                    return events.get(childPosition);
                case PERSON_EXPANDABLE_LIST_VIEW_TYPE:
                    return immediate_family.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.activity_person_expandable_group, parent, false);
            }

            TextView groupView = convertView.findViewById(R.id.GroupTitle);

            switch (groupPosition) {
                case EVENT_EXPANDABLE_LIST_VIEW_TYPE:
                    groupView.setText(R.string.EventExpandableGroupTitle);
                    break;
                case PERSON_EXPANDABLE_LIST_VIEW_TYPE:
                    groupView.setText(R.string.PersonExpandableGroupTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case EVENT_EXPANDABLE_LIST_VIEW_TYPE:
                    itemView = getLayoutInflater().inflate(R.layout.event, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_EXPANDABLE_LIST_VIEW_TYPE:
                    itemView = getLayoutInflater().inflate(R.layout.person, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            // BIRTH: Osaka, Japan (2000)
            TextView eventDetailsView = eventItemView.findViewById(R.id.TopEventInfo);
            Event event = events.get(childPosition);
            String event_details = event.getEventType().toUpperCase() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
            eventDetailsView.setText(event_details);

            // Jay Cui
            TextView associatedPersonNameView = eventItemView.findViewById(R.id.BottomEventInfo);
            Person associatedPerson = DataCache.getInstance().getPerson(event.getPersonID());
            String associated_person_name = associatedPerson.getFirstName() + " " + associatedPerson.getLastName();
            associatedPersonNameView.setText(associated_person_name);

            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(EVENT_ID, event.getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializePersonView(View personItemView, final int childPosition) {
            Person family_member = immediate_family.get(childPosition);

            ImageView genderView = personItemView.findViewById(R.id.GenderIcon);
            String gender = family_member.getGender();
            if (gender.equals("f")) {
                genderView.setImageResource(R.drawable.symbol_female);
            }
            else if (gender.equals("m")) {
                genderView.setImageResource(R.drawable.symbol_male);
            }

            TextView personNameView = personItemView.findViewById(R.id.TopPersonInfo);
            String person_name = family_member.getFirstName() + " " + family_member.getLastName();
            personNameView.setText(person_name);

            TextView relationshipView = personItemView.findViewById(R.id.BottomPersonInfo);
            String relationship;
            if (father != null && family_member.getPersonID().equals(father.getPersonID())) {
                relationship = "Father";
            }
            else if (mother != null && family_member.getPersonID().equals(mother.getPersonID())) {
                relationship = "Mother";
            }
            else if (spouse != null && family_member.getPersonID().equals(spouse.getPersonID())) {
                relationship = "Spouse";
            }
            else {
                relationship = "Child";
            }
            relationshipView.setText(relationship);

            personItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PERSON_ID, family_member.getPersonID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
