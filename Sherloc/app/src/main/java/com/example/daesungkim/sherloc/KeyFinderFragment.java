package com.example.daesungkim.sherloc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by daeseongkim on 2015. 12. 2..
 */
public class KeyFinderFragment extends Fragment {

    Button _confirmButton;
    EditText _specialIDField;
    TextView _ownerText;

    static TextView _bountyshow;
    static TextView _messageshow;
    static Button _accept;
    static Button _decline;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.key_finder_fragment, container, false);
        _confirmButton = (Button)rootView.findViewById(R.id.getContact);
        _specialIDField = (EditText) rootView.findViewById(R.id.keyField);
        _ownerText = (TextView) rootView.findViewById(R.id.ownerText);

        _confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String specialID;
                specialID=_specialIDField.getText().toString();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("object");
                query.whereEqualTo("isLost", true);
                query.whereEqualTo("objectID", specialID);
                query.include("owner");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(final List<ParseObject> IDList, ParseException e) {
                        if (e == null) {
                            if (IDList.size() == 1) {
                                ParseObject owner = IDList.get(0).getParseObject("owner");
                                final String name = owner.getString("username");
                                IDList.get(0).put("Finder", ParseUser.getCurrentUser().get("username"));
                                IDList.get(0).saveInBackground();

                                showDialog(getContext(), IDList.get(0), name);
                                }
                            }
                            else{
                                _ownerText.setText("ID is invalid or Owner's object is not lost!");
                            }
                        }
                    });
                }
        });

        return rootView;
    }

    public void showDialog(final Context context, final ParseObject obj, final String name){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.bountyshow);

        _bountyshow = (TextView) dialog.findViewById(R.id.bountyshow);
        _messageshow = (TextView) dialog.findViewById(R.id.messageshow);
        _accept = (Button) dialog.findViewById(R.id.accept);
        _decline = (Button) dialog.findViewById(R.id.decline);

        _bountyshow.setText("Bounty: " + obj.getString("Bounty"));
        _messageshow.setText("Message from owner: " + obj.getString("Message"));

        _accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. create an intent pass class name or intnet action name
                Intent intent = new Intent(context, ChatActivity.class);
                // 2. put key/value data
                intent.putExtra("Id", name);
                // 5. start the activity
                startActivity(intent);
                dialog.dismiss();
            }
        });

        _decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. create an intent pass class name or intnet action name
                Intent intent = new Intent(context, ChatActivity.class);
                // 2. put key/value data
                intent.putExtra("Id", name);
                // 5. start the activity
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
