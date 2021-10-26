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

public class secondListAdapter extends ArrayAdapter<secondList> {

    //important variables
    private int mostRecentlyClickedPosition;



    public secondListAdapter(Activity context, ArrayList<secondList> theList) {
        super(context, 0, theList);
        mostRecentlyClickedPosition = -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(this.getContext()).inflate(R.layout.second_list_layout, parent, false);
        }
        //modify indexTextView2 to display the index/number (ie "1. or 2.)
        String indexHelp;
        TextView indexTV = listItemView.findViewById(R.id.indexTextView2);
        indexHelp = String.valueOf(position + 1).concat(".");
        indexTV.setText(indexHelp);
        //modify nameOfItemTextView2 to display the name of an item in the parent list
        String nameHelp;
        TextView nameTV = listItemView.findViewById(R.id.nameOfItemTextView2);
        secondList currentItem = getItem(position);
        nameHelp = currentItem.getNameOfItem();
        nameTV.setText(nameHelp);
        //get ready to modify visibility
        Button hideButton2 = listItemView.findViewById(R.id.hideButton2);
        Button deleteButton2 = listItemView.findViewById(R.id.deleteButton2);
        Button moveButton2 = listItemView.findViewById(R.id.moveButton2);
        TextView moveToTextView2 = listItemView.findViewById(R.id.moveToTextView2);
        EditText positionEditText2 = listItemView.findViewById(R.id.positionEditText2);
        //if clicked make visible
        if (position == mostRecentlyClickedPosition) {
            hideButton2.setVisibility(View.VISIBLE);
            deleteButton2.setVisibility(View.VISIBLE);
            moveButton2.setVisibility(View.VISIBLE);
            moveToTextView2.setVisibility(View.VISIBLE);
            positionEditText2.setVisibility(View.VISIBLE);
        }
        //else make ~disappear~ ... (GONE)
        else {
            hideButton2.setVisibility(View.GONE);
            deleteButton2.setVisibility(View.GONE);
            moveButton2.setVisibility(View.GONE);
            moveToTextView2.setVisibility(View.GONE);
            positionEditText2.setVisibility(View.GONE);
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

}

