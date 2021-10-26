package com.example.todolist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class firstListAdapter extends ArrayAdapter<firstList>{

    //important variables
    private int mostRecentlyClickedPosition;
    private String parentName;


    public firstListAdapter(Activity context, ArrayList<firstList> theList) {
        super(context,0,theList);
        mostRecentlyClickedPosition = -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.first_list_layout, parent, false);
        }
        //modify indexTextView1 to display the index/number (ie "1. or 2.)

        String indexHelp;
        TextView indexTV = listItemView.findViewById(R.id.indexTextView1);
        indexHelp = String.valueOf(position+1).concat(".");
        indexTV.setText(indexHelp);
        //modify nameOfListTextView1 to display the list name
        String nameHelp;
        TextView nameTV = listItemView.findViewById(R.id.nameOfListTextView1);
        firstList currentList = getItem(position);
        nameHelp = currentList.getNameOfList();
        nameTV.setText(nameHelp);
        //get ready to modify visibility
        Button selectButton1 = listItemView.findViewById(R.id.selectButton1);
        Button hideButton1 = listItemView.findViewById(R.id.hideButton1);
        Button deleteButton1 = listItemView.findViewById(R.id.deleteButton1);
        Button moveButton1 = listItemView.findViewById(R.id.moveButton1);
        TextView moveToTextView1 = listItemView.findViewById(R.id.moveToTextView1);
        EditText positionEditText1 = listItemView.findViewById(R.id.positionEditText1);
        //if clicked make visible
        if (position == mostRecentlyClickedPosition) {
            selectButton1.setVisibility(View.VISIBLE);
            hideButton1.setVisibility(View.VISIBLE);
            deleteButton1.setVisibility(View.VISIBLE);
            moveButton1.setVisibility(View.VISIBLE);
            moveToTextView1.setVisibility(View.VISIBLE);
            positionEditText1.setVisibility(View.VISIBLE);
        }
        //else make ~disappear~ ... (GONE)
        else {
            selectButton1.setVisibility(View.GONE);
            hideButton1.setVisibility(View.GONE);
            deleteButton1.setVisibility(View.GONE);
            moveButton1.setVisibility(View.GONE);
            moveToTextView1.setVisibility(View.GONE);
            positionEditText1.setVisibility(View.GONE);
        }
        return listItemView;
    }

    /**
     * method that is executed when called
     * @param mostRecentlyClickedPosition the integer variable that stores the value
     * of the most recently clicked item in the ListView.
     * Basically a setter.
     */
    public void setMostRecentlyClickedPosition(int mostRecentlyClickedPosition) {
        this.mostRecentlyClickedPosition = mostRecentlyClickedPosition;
    }

    /**
     * method that is executed when called
     * @return returns the integer that corresponds to the most recently
     * clicked item in the ListView. Basically a getter.
     */
    public int getMostRecentlyClickedPosition() {
        return mostRecentlyClickedPosition;
    }


    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}

