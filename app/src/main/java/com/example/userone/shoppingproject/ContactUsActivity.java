package com.example.userone.shoppingproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactUsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
    String selected_spinner_item;
    List<String> user_queries;
    Button btn_submit;
    EditText ed_name, ed_email;
    Spinner sp_spinner;
    String user_name, user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Log.d("am in ","ContactUsActivity");

        sp_spinner = (Spinner) findViewById(R.id.sp_select);
        user_queries = new ArrayList<>();
        btn_submit = (Button) findViewById(R.id.contact_btn_submit);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_email = (EditText) findViewById(R.id.ed_email);

        insertQueryItemsToList();
        DisplayUserQueriesListToSpinner();

    }

    private void DisplayUserQueriesListToSpinner() {
        ArrayAdapter<String> rooms_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user_queries);
        rooms_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_spinner.setAdapter(rooms_adapter);
    }

    private void insertQueryItemsToList() {
        user_queries.add("Please Select");
        user_queries.add("Make a suggestion");
        user_queries.add("Report a Post");
        user_queries.add("File a Complaint");
        user_queries.add("Advertise on your site");
        user_queries.add("Ask a Question");
        user_queries.add("Leave a Compliment");
    }

    private void getInputValues() {
        user_name = ed_name.getText().toString().trim();
        user_email = ed_email.getText().toString().trim();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_spinner_item = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
