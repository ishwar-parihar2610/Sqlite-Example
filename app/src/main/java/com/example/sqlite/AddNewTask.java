package com.example.sqlite;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.sqlite.Utils.DataBaseHelper;
import com.example.sqlite.adapter.TodoAdapter;
import com.example.sqlite.databinding.AddNewTaskBinding;
import com.example.sqlite.model.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG="AddNewTask";
    private DataBaseHelper db;
    private LayoutInflater inflater;
    private AddNewTaskBinding binding;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      binding=DataBindingUtil.inflate(inflater,R.layout.add_new_task,container,false);
      return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db=new DataBaseHelper(getActivity());
        boolean isUpdate=false;
        Bundle bundle=getArguments();
        if (bundle!=null){
            isUpdate=true;
            String task=bundle.getString("task");
            binding.editText.setText(task);
            if (task.length()>0){
                binding.buttonSave.setEnabled(false);

            }
        }
            binding.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    binding.buttonSave.setEnabled(false);
                    binding.buttonSave.setBackgroundColor(Color.GRAY);
                }else{
                    binding.buttonSave.setEnabled(true);
                    binding.buttonSave.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        boolean finalIsUpdate = isUpdate;
        binding.buttonSave.setOnClickListener(v -> {
            String text=binding.editText.getText().toString();
            if (finalIsUpdate){
                db.updateTask(bundle.getInt("id"),text);

            }else{
                ToDoModel item=new ToDoModel();
                item.setTask(text);
                item.setStatus(0);
                db.insertTask(item);
            }
            dismiss();
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity=getActivity();
        if (activity instanceof OnDialogCloseListener){
            ((OnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
