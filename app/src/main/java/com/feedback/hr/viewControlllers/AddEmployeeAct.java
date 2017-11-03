package com.feedback.hr.viewControlllers;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.feedback.hr.model.Employee;
import com.feedback.hr.R;
import com.feedback.hr.utilities.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

/**
 * Created by Aditi on 7/20/2017.
 */

public class AddEmployeeAct extends Activity implements View.OnClickListener {
    private EditText edEmpName;
    private EditText edEmpPass;
    private EditText edEmEmail;
    private Button btnAddEmp;
    private EditText edEmpCPass;
    private DatabaseReference emp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employee);
        edEmpName = (EditText) findViewById(R.id.edEmpName);
        edEmEmail = (EditText) findViewById(R.id.edEmEmail);
        edEmpPass = (EditText) findViewById(R.id.edEmpPass);
        edEmpCPass = (EditText) findViewById(R.id.edEmpCPass);
        btnAddEmp = (Button) findViewById(R.id.btnAddEmp);
        ImageView imvBack = (ImageView) findViewById(R.id.imvBack);
        imvBack.setOnClickListener(this);
        btnAddEmp.setOnClickListener(this);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        emp = ref.child("employee");
    }


    @Override
    public void onClick(View view) {
        int vId = view.getId();
        switch (vId) {
            case R.id.btnAddEmp:
                Util.showProDialog(this);
                addEmployee();
                break;
            case R.id.imvBack:
                onBackPressed();
                break;
        }
    }

    private void addEmployee() {

        final String empName = edEmpName.getText().toString().trim();
        final String empEmail = edEmEmail.getText().toString().trim();
        final String empPass = edEmpPass.getText().toString().trim();
        String empCPass = edEmpCPass.getText().toString().trim();
        if (isUserValid(empName, empEmail, empPass, empCPass)) {
            emp.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String email = empEmail.replace(".", ",");
                            if (data.getKey().equals(email)) {
                                edEmEmail.setError("Email Id already exists.");
                            } else {
                                Employee employee = new Employee();
                                employee.empEmail = empEmail;
                                employee.empName = empName;
                                employee.empPassword = empPass;
                                emp.child(email).push();
                                employeeAdded();
                            }
                        }
                    } else {
                        String email = empEmail.replace(".", ",");
                        Employee employee = new Employee();
                        employee.empEmail = empEmail;
                        employee.empName = empName;
                        employee.empPassword = empPass;
                        emp.child(email).push();
                        employeeAdded();
                    }
                    Util.dismissProDialog();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Aditi", databaseError.getMessage());
                }

            });
            String email = empEmail.replace(".", ",");
            Employee employee = new Employee();
            employee.empEmail = empEmail;
            employee.empName = empName;
            employee.empPassword = empPass;
            emp.child(email).setValue(employee);

        }
    }

    private void employeeAdded() {
        Util.showToast(AddEmployeeAct.this, "Employee Added Successfully.");
        edEmpName.setText("");
        edEmEmail.setText("");
        edEmpPass.setText("");
        edEmpCPass.setText("");
    }

    public boolean isUserValid(String empName, String empEmail, String empPass, String empCPass) {
        boolean isValid = true;
        String regx = "^[\\p{L} .'-]+$";
        if (TextUtils.isEmpty(empName)) {
            isValid = false;
            edEmpName.setError(getResources().getString(R.string.emp_name_blank));
        } else if (!Pattern.matches(regx, empName)) {
            isValid = false;
            edEmpName.setError(getResources().getString(R.string.emp_name_invalid));
        } else if (TextUtils.isEmpty(empEmail)) {
            isValid = false;
            edEmEmail.setError(getResources().getString(R.string.emp_email_blank));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(empEmail).matches()) {
            isValid = false;
            edEmEmail.setError(getResources().getString(R.string.emp_email_invalid));
        } else if (TextUtils.isEmpty(empPass)) {
            isValid = false;
            edEmpPass.setError(getResources().getString(R.string.password_blank));
        } else if (TextUtils.isEmpty(empCPass)) {
            isValid = false;
            edEmpCPass.setError(getResources().getString(R.string.confirm_password_blank));
        } else if (!isPasswordValid(empPass)) {
            isValid = false;
        } else if (!empPass.equals(empCPass)) {
            isValid = false;
            edEmpCPass.setError(getResources().getString(R.string.password_not_matches));
        }
        return isValid;
    }

    public boolean isPasswordValid(String empPass) {
        if (empPass.length() < 8 || empPass.length() > 15) {
            edEmpPass.setError(getResources().getString(R.string.password_lenngth));
            return false;
        } else if (!empPass.matches(".*\\d.*")) {
            edEmpPass.setError(getResources().getString(R.string.digits_for_password));
            return false;
        } else if (!empPass.matches(".*[a-z].*")) {
            edEmpPass.setError(getResources().getString(R.string.lower_case_password));
            return false;
        } else if (!empPass.matches(".*[@#$*].*")) {
            edEmpPass.setError(getResources().getString(R.string.special_character_password));
            return false;
        }
        return true;
    }
}
