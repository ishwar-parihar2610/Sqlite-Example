package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.example.sqlite.Utils.DataBaseHelper;
import com.example.sqlite.adapter.TodoAdapter;
import com.example.sqlite.databinding.ActivityMainBinding;
import com.example.sqlite.model.ToDoModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {
    private ActivityMainBinding binding;
    private DataBaseHelper myDb;
    private List<ToDoModel> mList=new ArrayList<>();
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        myDb = new DataBaseHelper(MainActivity.this);
        todoAdapter = new TodoAdapter(myDb, MainActivity.this);
        binding.todoRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mList= myDb.getAllTask();
        todoAdapter.setTask(mList);
        binding.todoRecycleView.setAdapter(todoAdapter);
        binding.addTask.setOnClickListener(v -> {
        AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
        });
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecycleViewTouchHelper(todoAdapter));
        itemTouchHelper.attachToRecyclerView(binding.todoRecycleView);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.addAll(myDb.getAllTask());
        Collections.reverse(mList);
        todoAdapter.setTask(mList);
        Log.e("TAG", "size"+ mList.size());

    }
}