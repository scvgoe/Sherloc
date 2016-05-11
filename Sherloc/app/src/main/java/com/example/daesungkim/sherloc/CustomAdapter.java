package com.example.daesungkim.sherloc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    String tempValues=null;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }




    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public ImageButton chaticon;
        public Switch toggleButton;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tab_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.objectName);

            holder.toggleButton=(Switch)vi.findViewById(R.id.isLost);
            holder.chaticon = (ImageButton) vi.findViewById(R.id.chaticon);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues =  data.get( position ).toString();

            /************  Set Model values in Holder elements ***********/

            holder.text.setText(tempValues);
            final Switch toggle = holder.toggleButton;

            /******** Set Item Click Listner for LayoutInflater for each row *******/
            ParseQuery<ParseObject> query = ParseQuery.getQuery("object");
            query.whereEqualTo("objectName", data.get(position).toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        System.out.println(objects.get(0).getString("objectName") + "objects:" + objects.get(0).getBoolean("isLost"));
                        //return boolean to check whether is lost
                        if (objects.get(0).getBoolean("isLost")) {
                            toggle.setChecked(true);
                            System.out.println(objects.get(0).getBoolean("isLost"));
                        } else {
                            toggle.setChecked(false);
                            System.out.println(objects.get(0).getBoolean("isLost"));
                        }

                    } else {
                        Log.d("message", e.getMessage());
                    }
                }
            });

            holder.toggleButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("object");
                    System.out.println(position);
                    query.whereEqualTo("objectName", data.get(position).toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(final List<ParseObject> objectsList, ParseException e) {
                            Boolean onOff = objectsList.get(0).getBoolean("isLost");
                            if (onOff) {
                                Toast.makeText(activity.getApplicationContext(), objectsList.get(0).getString("objectName") + "is Found", Toast.LENGTH_SHORT).show();

                                objectsList.get(0).put("isLost", false);
                                objectsList.get(0).saveInBackground();
                            } else {
                                Toast.makeText(activity.getApplicationContext(), objectsList.get(0).getString("objectName") + "is Lost", Toast.LENGTH_SHORT).show();
                                objectsList.get(0).put("isLost", true);
                                HomeActivity2.showDialog(activity, objectsList.get(0));
                                objectsList.get(0).saveInBackground();
                            }

                        }
                    });

                }
            });


            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("object");
            query1.whereEqualTo("objectName", tempValues);
            query1.findInBackground(new FindCallback<ParseObject>() {
                public void done(final List<ParseObject> objectList, ParseException e) {
                    if (e == null) {

                        if (objectList.get(0).getString("Finder") != null) {
                            System.out.println(objectList.get(0).getString("Finder"));
                            holder.chaticon.setVisibility(View.VISIBLE);
                            holder.chaticon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println(objectList.get(0).getString("Finder"));
                                    Toast.makeText(activity.getApplicationContext(), objectList.get(0).getString("Finder"), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(activity.getApplicationContext(), ChatActivity.class);
                                    // 2. put key/value data
                                    intent.putExtra("Id", objectList.get(0).getString("Finder"));
                                    // 5. start the activity
                                    activity.startActivity(intent);
                                }
                            });
                        } else {
                            holder.chaticon.setVisibility(View.INVISIBLE);
                        }


                    } else {
                        Log.d("message", "Error: " + e.getMessage());
                    }
                }
            });
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }
}