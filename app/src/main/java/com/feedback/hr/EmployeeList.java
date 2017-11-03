package com.feedback.hr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.feedback.hr.model.Employee;
import com.feedback.hr.viewControlllers.AddEmployeeAct;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Aditi on 7/20/2017.
 */

public class EmployeeList extends Activity implements View.OnClickListener {

    private ListView lvEmpList;
    private FirebaseListAdapter<Employee> adapter;
    private FloatingActionButton fbAddEmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_list);
        lvEmpList = (ListView) findViewById(R.id.lvEmpList);
        fbAddEmp = (FloatingActionButton) findViewById(R.id.fbAddEmp);
        fbAddEmp.setOnClickListener(this);
        adapter = new FirebaseListAdapter<Employee>(this, Employee.class,
                R.layout.employee_row, FirebaseDatabase.getInstance().getReference().child("employee")) {
            @Override
            protected void populateView(View v, Employee model, int position) {
                // Get references to the views of message.xml
                TextView txtName = (TextView) v.findViewById(R.id.txtName);
                TextView txtEmail = (TextView) v.findViewById(R.id.txtEmail);
                DatabaseReference val = FirebaseDatabase.getInstance().getReference("employee").child(model.empName);
                txtName.setText(model.empName);
                txtEmail.setText(model.empEmail);
            }
        };

        lvEmpList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.fbAddEmp:
                Intent intent = new Intent(this, AddEmployeeAct.class);
                startActivity(intent);
        }
    }
}
