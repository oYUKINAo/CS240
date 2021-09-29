package com.example.familymapclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joanzapata.iconify.IconDrawable;

import java.util.List;

import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_SEARCH_RESULT_VIEW_TYPE = 0;
    private static final int EVENT_SEARCH_RESULT_VIEW_TYPE = 1;

    private EditText searchEditText;
    private static String searchString = "^_^";

    PersonEventAdapter adapter;

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            searchString = searchEditText.getText().toString();
            if (searchString.isEmpty()) {
                searchString = "^_^";
            }

            DataCache cache = DataCache.getInstance();
            List<Person> people = cache.getPeopleByRegex(searchString);
            List<Event> events = cache.getEventsByRegex(searchString);

            adapter.setPeopleAndEvents(people, events);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchEditText = (EditText) findViewById(R.id.SearchEditText);
        searchEditText.addTextChangedListener(textWatcher);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        DataCache cache = DataCache.getInstance();
        List<Person> people = cache.getPeopleByRegex(searchString);
        List<Event> events = cache.getEventsByRegex(searchString);

        adapter = new PersonEventAdapter(people, events);
        recyclerView.setAdapter(adapter);
    }

    private class PersonEventAdapter extends RecyclerView.Adapter<PersonEventHolder> {
        private List<Person> people;
        private List<Event> events;

        public PersonEventAdapter(List<Person> people, List<Event> events) {
            this.people = people;
            this.events = events;
        }

        public void setPeopleAndEvents(List<Person> people, List<Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_SEARCH_RESULT_VIEW_TYPE : EVENT_SEARCH_RESULT_VIEW_TYPE;
        }

        @NonNull
        @Override
        public PersonEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_SEARCH_RESULT_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.event, parent, false);
            }

            return new PersonEventHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonEventHolder holder, int position) {
            if (position < people.size()) {
                holder.bind(people.get(position));
            }
            else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class PersonEventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String PERSON_ID = "personID";
        private static final String EVENT_ID = "eventID";

        private final ImageView gender;
        private final TextView personName;

        private final ImageView coordinate;
        private final TextView eventDetails;
        private final TextView associatedPersonName;

        private final int viewType;

        private Person person;

        private Event event;
        private Person associatedPerson;

        public PersonEventHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == PERSON_SEARCH_RESULT_VIEW_TYPE) {
                gender = itemView.findViewById(R.id.GenderIcon);
                personName = itemView.findViewById(R.id.TopPersonInfo);

                coordinate = null;
                eventDetails = null;
                associatedPersonName = null;
            }
            else {
                gender = null;
                personName = null;

                coordinate = itemView.findViewById(R.id.LocationIcon);
                eventDetails = itemView.findViewById(R.id.TopEventInfo);
                associatedPersonName = itemView.findViewById(R.id.BottomEventInfo);
            }
        }

        public void bind(Person person) {
            this.person = person;

            if (this.person.getGender().equals("f")) {
                gender.setImageResource(R.drawable.symbol_female);
            }
            else if (this.person.getGender().equals("m")) {
                gender.setImageResource(R.drawable.symbol_male);
            }

            String person_name = this.person.getFirstName() + " " + this.person.getLastName();
            personName.setText(person_name);
        }

        public void bind(Event event) {
            this.event = event;
            this.associatedPerson = DataCache.getInstance().getPerson(event.getPersonID());

            String event_details = this.event.getEventType().toUpperCase() + ": " + this.event.getCity() + ", " + this.event.getCountry() + " (" + this.event.getYear() + ")";
            eventDetails.setText(event_details);

            String associated_person_name = this.associatedPerson.getFirstName() + " " + this.associatedPerson.getLastName();
            associatedPersonName.setText(associated_person_name);
        }

        @Override
        public void onClick(View view) {
            if (viewType == PERSON_SEARCH_RESULT_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PERSON_ID, this.person.getPersonID());
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EVENT_ID, this.event.getEventID());
                startActivity(intent);
            }
        }
    }
}
