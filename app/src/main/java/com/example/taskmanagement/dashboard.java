package com.example.taskmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    private Spinner statusSpinner;
    private Spinner prioritySpinner;
    private EditText searchEditText;
    private Button newTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        statusSpinner = findViewById(R.id.statusSpinner);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        searchEditText = findViewById(R.id.searchEditText);
        newTaskButton = findViewById(R.id.newTaskButton);

        // Set up RecyclerView
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);

        // Set up Spinners
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_options, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_options, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        // Set up button click listener
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle new task button click
                Toast.makeText(dashboard.this, "New Task Button Clicked", Toast.LENGTH_SHORT).show();
                // Add your logic to create a new task here
            }
        });

        // Set up spinner item selection listeners
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
                filterTasksByStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPriority = parent.getItemAtPosition(position).toString();
                filterTasksByPriority(selectedPriority);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up search functionality
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchEditText.getText().toString();
            filterTasksBySearchQuery(query);
            return true;
        });

        // Apply window insets

    }

    private void filterTasksByStatus(String status) {
        // Implement your logic to filter tasks by status
        Toast.makeText(this, "Filter by status: " + status, Toast.LENGTH_SHORT).show();
    }

    private void filterTasksByPriority(String priority) {
        // Implement your logic to filter tasks by priority
        Toast.makeText(this, "Filter by priority: " + priority, Toast.LENGTH_SHORT).show();
    }

    private void filterTasksBySearchQuery(String query) {
        // Implement your logic to filter tasks by search query
        Toast.makeText(this, "Search query: " + query, Toast.LENGTH_SHORT).show();
    }
}