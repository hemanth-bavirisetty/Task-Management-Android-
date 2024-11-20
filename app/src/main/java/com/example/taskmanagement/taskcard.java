package com.example.taskmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class taskcard extends AppCompatActivity {

    private TextView taskTitle;
    private TextView taskDescription;
    private TextView taskPriority;
    private Spinner taskStatus;
    private TextView taskDeadline;
    private ImageButton threeDotMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_taskcard);

        // Initialize views
        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDescription);
        taskPriority = findViewById(R.id.taskPriority);
        taskStatus = findViewById(R.id.taskStatus);
        taskDeadline = findViewById(R.id.taskDeadline);
        threeDotMenu = findViewById(R.id.threeDotMenu);

        // Set up any listeners or data binding here
        setupListeners();

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupListeners() {
        // Example: Set a click listener on the three dot menu
        threeDotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., show a menu
                showOverflowMenu();
            }
        });

        // Example: Set a listener for the status spinner
        taskStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected status
                String selectedStatus = parent.getItemAtPosition(position).toString();
                updateTaskStatus(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void showOverflowMenu() {
        // Implement the logic to show the overflow menu
        // For example, you can use a PopupMenu or a custom dialog
    }

    private void updateTaskStatus(String status) {
        // Implement the logic to update the task status
        // For example, you can update the UI or save the status to a database
    }
}