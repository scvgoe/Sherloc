package com.example.daesungkim.sherloc;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ObjectRegistrationActivity extends AppCompatActivity {
    private static final String TAG = "ObjectRegistrationActivity";
    public Button confirm;
    public EditText _objectID;
    public EditText _objectName;
    public Spinner _objectTypeSpinner;
    public Button _generateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_registration);
        _objectTypeSpinner= (Spinner)findViewById(R.id.spinner);
        String [] objectType = {"Sticker","Beacon"};
        ArrayAdapter<String> array = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,objectType);
        array.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _objectTypeSpinner.setAdapter(array);

        confirm = (Button) findViewById(R.id.button);
        _objectID = (EditText) findViewById(R.id.idText);
        _objectName = (EditText) findViewById(R.id.nameText);
        _generateKey = (Button) findViewById(R.id.create_key);

        _generateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneratingSpecialID generatingSpecialID = new GeneratingSpecialID();
                String key = generatingSpecialID.generateSpecialID();
                _objectID.setText(key);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                registration();
                Toast.makeText(getApplicationContext(), "Scroll to update list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registration() {
        Log.d(TAG, "Registration");

        confirm.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ObjectRegistrationActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();


        final String objectID = _objectID.getText().toString();
        final String objectName = _objectName.getText().toString();
        final String objectType = _objectTypeSpinner.getSelectedItem().toString();

        final ParseObject object = new ParseObject("object");
        object.put ("objectID", objectID);
        object.put("objectName", objectName);
        object.put("objectType", objectType);

        final ParseUser currentUser = ParseUser.getCurrentUser();
        object.put("owner", currentUser);
        object.put("isLost", false);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("object");
        query.whereEqualTo("objectID", objectID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> IDList, ParseException e) {
                if (e == null) {
                    if (IDList.size() == 0) {
                        object.saveInBackground();

                        Toast.makeText(getApplicationContext(), objectName + " is registered to " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Special ID existed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        finish();
                        progressDialog.dismiss();
                    }
                }, 3000);


    }
}
