package com.example.taskmanagement;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFormDialog extends Dialog {

    private Task task;
    private TaskFormListener listener;
    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;
    private AutoCompleteTextView priorityDropdown;
    private AutoCompleteTextView statusDropdown;
    private TextInputEditText deadlineEditText;

    public TaskFormDialog(@NonNull Context context, Task task) {
        super(context);
        this.task = task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_task_form);

        TextView dialogTitle = findViewById(R.id.dialogTitle);
        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        priorityDropdown = findViewById(R.id.dropdownPriority);
        statusDropdown = findViewById(R.id.dropdownStatus);
        deadlineEditText = findViewById(R.id.editTextDeadline);
        Button cancelButton = findViewById(R.id.buttonCancel);
        Button submitButton = findViewById(R.id.buttonSubmit);

        setupDropdowns();
        setupDatePicker();

        if (task != null) {
            dialogTitle.setText("Edit Task");
            titleEditText.setText(task.getTitle());
            descriptionEditText.setText(task.getDescription());
            priorityDropdown.setText(task.getPriority(), false);
            statusDropdown.setText(task.getStatus(), false);
            deadlineEditText.setText(task.getDeadline());
            submitButton.setText("Update Task");
        } else {
            dialogTitle.setText("Create New Task");
            submitButton.setText("Create Task");
        }

        cancelButton.setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> submitTask());
    }

    private void setupDropdowns() {
        String[] priorities = {"Low", "Medium", "High"};
        String[] statuses = {"Yet to start", "In progress", "Completed", "On hold"};

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, priorities);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, statuses);

        priorityDropdown.setAdapter(priorityAdapter);
        statusDropdown.setAdapter(statusAdapter);
    }

    private void setupDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select deadline")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        deadlineEditText.setOnClickListener(v -> datePicker.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "DATE_PICKER"));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            deadlineEditText.setText(sdf.format(new Date(selection)));
        });
    }

    private void submitTask() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priority = priorityDropdown.getText().toString();
        String status = statusDropdown.getText().toString();
        String deadline = deadlineEditText.getText().toString();

        if (title.isEmpty() || description.isEmpty() || priority.isEmpty() || status.isEmpty() || deadline.isEmpty()) {
            // Show error message
            return;
        }

        if (task == null) {
            task = new Task(UUID.randomUUID().toString(), title, description, priority, status, deadline);
            listener.onTaskCreated(task);
        } else {
            task.setTitle(title);
            task.setDescription(description);
            task.setPriority(priority);
            task.setStatus(status);
            task.setDeadline(deadline);
            listener.onTaskUpdated(task);
        }

        dismiss();
    }

    public void setTaskFormListener(TaskFormListener listener) {
        this.listener = listener;
    }

    public interface TaskFormListener {
        void onTaskCreated(Task task);
        void onTaskUpdated(Task task);
    }
}

