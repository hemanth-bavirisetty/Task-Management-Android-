package com.example.taskmanagement;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagement.Task;
import com.example.taskmanagement.TaskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity implements TaskAdapter.TaskItemListener {

    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private AutoCompleteTextView statusFilter;
    private AutoCompleteTextView priorityFilter;
    private TextInputEditText searchFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        statusFilter = findViewById(R.id.statusFilter);
        priorityFilter = findViewById(R.id.priorityFilter);
        searchFilter = findViewById(R.id.searchFilter);
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this);
        tasksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        tasksRecyclerView.setAdapter(taskAdapter);

        setupFilters();
        loadTasks();

        fabAddTask.setOnClickListener(v -> showTaskForm(null));
    }

    private void setupFilters() {
        String[] statusOptions = {"All", "Yet to start", "In progress", "Completed", "On hold"};
        String[] priorityOptions = {"All", "Low", "Medium", "High"};

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, statusOptions);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, priorityOptions);

        statusFilter.setAdapter(statusAdapter);
        priorityFilter.setAdapter(priorityAdapter);

        statusFilter.setOnItemClickListener((parent, view, position, id) -> filterTasks());
        priorityFilter.setOnItemClickListener((parent, view, position, id) -> filterTasks());
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterTasks();
            }
        });
    }

    private void loadTasks() {
        // In a real app, you would load tasks from a database or API
        taskList.add(new Task("1", "Complete project proposal", "Write and submit the project proposal", "high", "in-progress", "2023-06-30"));
        taskList.add(new Task("2", "Review code changes", "Review and merge pull requests", "medium", "yet-to-start", "2023-06-25"));
        taskList.add(new Task("3", "Prepare presentation", "Create slides for the team meeting", "low", "completed", "2023-06-20"));
        taskAdapter.notifyDataSetChanged();
        updateTaskStats();
    }

    private void filterTasks() {
        String status = statusFilter.getText().toString();
        String priority = priorityFilter.getText().toString();
        String search = searchFilter.getText().toString().toLowerCase();

        List<Task> filteredList = new ArrayList<>();
        for (Task task : taskList) {
            boolean matchesStatus = status.equals("All") || task.getStatus().equalsIgnoreCase(status);
            boolean matchesPriority = priority.equals("All") || task.getPriority().equalsIgnoreCase(priority);
            boolean matchesSearch = task.getTitle().toLowerCase().contains(search) || task.getDescription().toLowerCase().contains(search);

            if (matchesStatus && matchesPriority && matchesSearch) {
                filteredList.add(task);
            }
        }

        taskAdapter.updateTasks(filteredList);
        updateTaskStats();
    }

    private void updateTaskStats() {
        int totalTasks = taskList.size();
        int completedTasks = 0;
        int inProgressTasks = 0;
        int highPriorityTasks = 0;

        for (Task task : taskList) {
            if (task.getStatus().equalsIgnoreCase("completed")) {
                completedTasks++;
            } else if (task.getStatus().equalsIgnoreCase("in-progress")) {
                inProgressTasks++;
            }
            if (task.getPriority().equalsIgnoreCase("high")) {
                highPriorityTasks++;
            }
        }

        ((TextView) findViewById(R.id.totalTasksCount)).setText(String.valueOf(totalTasks));
        ((TextView) findViewById(R.id.completedTasksCount)).setText(String.valueOf(completedTasks));
        ((TextView) findViewById(R.id.inProgressTasksCount)).setText(String.valueOf(inProgressTasks));
        ((TextView) findViewById(R.id.highPriorityTasksCount)).setText(String.valueOf(highPriorityTasks));
    }

    private void showTaskForm(Task task) {
        TaskFormDialog dialog = new TaskFormDialog(this, task);
        dialog.setTaskFormListener(new TaskFormDialog.TaskFormListener() {
            @Override
            public void onTaskCreated(Task newTask) {
                taskList.add(newTask);
                filterTasks();
            }

            @Override
            public void onTaskUpdated(Task updatedTask) {
                int index = taskList.indexOf(task);
                if (index != -1) {
                    taskList.set(index, updatedTask);
                    filterTasks();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onTaskClick(Task task) {
        showTaskForm(task);
    }

    @Override
    public void onTaskStatusChange(Task task, String newStatus) {
        task.setStatus(newStatus);
        filterTasks();
    }

    @Override
    public void onTaskDelete(Task task) {
        taskList.remove(task);
        filterTasks();
    }
}

