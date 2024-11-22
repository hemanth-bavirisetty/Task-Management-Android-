package com.example.taskmanagement;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private TaskItemListener listener;

    public TaskAdapter(List<Task> tasks, TaskItemListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateTasks(List<Task> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        Chip priorityChip;
        Chip statusChip;
        TextView deadlineTextView;
        ImageButton moreOptionsButton;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTaskTitle);
            descriptionTextView = itemView.findViewById(R.id.textViewTaskDescription);
            priorityChip = itemView.findViewById(R.id.chipPriority);
            statusChip = itemView.findViewById(R.id.chipStatus);
            deadlineTextView = itemView.findViewById(R.id.textViewDeadline);
            moreOptionsButton = itemView.findViewById(R.id.buttonMoreOptions);
        }

        void bind(Task task) {
            titleTextView.setText(task.getTitle());
            descriptionTextView.setText(task.getDescription());
            priorityChip.setText(task.getPriority());
            statusChip.setText(task.getStatus());
            deadlineTextView.setText(task.getDeadline());

            itemView.setOnClickListener(v -> listener.onTaskClick(task));
            statusChip.setOnClickListener(v -> showStatusChangeDialog(task));
            moreOptionsButton.setOnClickListener(v -> showMoreOptionsDialog(task));
        }

        private void showStatusChangeDialog(Task task) {
            // Implement a dialog to change the task status
            // For simplicity, we'll just cycle through statuses here
            String[] statuses = {"yet-to-start", "in-progress", "completed", "on-hold"};
            int currentIndex = java.util.Arrays.asList(statuses).indexOf(task.getStatus());
            String newStatus = statuses[(currentIndex + 1) % statuses.length];
            listener.onTaskStatusChange(task, newStatus);
        }

        private void showMoreOptionsDialog(Task task) {
            // Implement a dialog with more options (e.g., edit, delete)
            // For simplicity, we'll just show a delete option here
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Task Options")
                    .setItems(new CharSequence[]{"Delete"}, (dialog, which) -> {
                        if (which == 0) {
                            listener.onTaskDelete(task);
                        }
                    })
                    .show();
        }
    }

    public interface TaskItemListener {
        void onTaskClick(Task task);
        void onTaskStatusChange(Task task, String newStatus);
        void onTaskDelete(Task task);
    }
}

