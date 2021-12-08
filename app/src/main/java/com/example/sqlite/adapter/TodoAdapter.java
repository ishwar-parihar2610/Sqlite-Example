package com.example.sqlite.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite.AddNewTask;
import com.example.sqlite.MainActivity;
import com.example.sqlite.R;
import com.example.sqlite.Utils.DataBaseHelper;
import com.example.sqlite.databinding.TaskLayoutBinding;
import com.example.sqlite.model.ToDoModel;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private List<ToDoModel> mList;
    private MainActivity mainActivity;
    private DataBaseHelper myDb;

    public TodoAdapter(DataBaseHelper myDb, MainActivity activity) {
        this.mainActivity = activity;
        this.myDb = myDb;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return new MyViewHolder(DataBindingUtil.inflate(inflater, R.layout.task_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = mList.get(position);
        holder.binding.checkBox.setText(toDoModel.getTask());
        holder.binding.checkBox.setChecked(toBoolean(toDoModel.getStatus()));
        holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                myDb.updateStatus(toDoModel.getId(), 1);
            } else {
                myDb.updateStatus(toDoModel.getId(), 0);
            }
        });

    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return mainActivity;
    }

    //add Task To List
    public void setTask(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();


    }

    //delete Task To List
    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDb.deleteTask(item.getId());
        mList.remove(item.getId());
        notifyItemRemoved(position);
    }

    //public void editItem
    public void editItem(int position) {
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask task=new AddNewTask();
        task.setArguments(bundle);
        task.show(mainActivity.getSupportFragmentManager(),task.getTag());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TaskLayoutBinding binding;

        public MyViewHolder(TaskLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;

        }
    }
}
