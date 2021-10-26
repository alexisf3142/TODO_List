package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    //important variables
    ArrayList<firstList> theList = new ArrayList<firstList>();
    ArrayList<String> theListString = new ArrayList<String>();
    firstListAdapter itemAdapter;
    ListView firstListView;


    //onItemClickListener
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            itemAdapter.setMostRecentlyClickedPosition(i);
            itemAdapter.notifyDataSetChanged();

            //closing the keyboard because otherwise it glitches ¯\_(ツ)_/¯
            //I don't know why, I wish I knew why...
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstListView = findViewById(R.id.LOLListView);
        //set up adapter + on item click listener
        itemAdapter = new firstListAdapter(this, theList);
        firstListView.setAdapter(itemAdapter);
        firstListView.setOnItemClickListener(listener);
        //read the file and update screen if needed
        readFile();
        updateTextBasedOnSize();
        itemAdapter.notifyDataSetChanged();
        //double check visibility
        EditText listNameET = findViewById(R.id.listNameEditText);
        Button addButton = findViewById(R.id.addButton);
        listNameET.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);

        itemAdapter.notifyDataSetChanged();
    }

    /**
     * method that is executed when the addListButton1 is clicked
     *
     * @param view the view that was clicked (button)
     *             this changes the visibility of the listElementEditText1 & addButton1
     */
    public void addListButton(View view) {
        EditText listNameET = findViewById(R.id.listNameEditText);
        Button addButton = findViewById(R.id.addButton);
        listNameET.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
    }

    /**
     * method that is executed when the addButton1 is clicked
     *
     * @param view the view that was clicked (button)
     *             takes the user input and add it to theList, and updates the file
     */
    public void addButton1(View view) {
        //makes keyboard go away ~magical incantation~
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //check for validity
        EditText listNameET = findViewById(R.id.listNameEditText);
        boolean duplicate = false;
        boolean empty = false;
        for (int i = 0; i < theList.size(); i++) {
            if (listNameET.getText().toString().equals(theList.get(i).getNameOfList())) {
                Toast notValidToast = Toast.makeText(this, "a list by that name already exists", Toast.LENGTH_LONG);
                notValidToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                notValidToast.show();
                duplicate = true;
                listNameET.setText("");
                itemAdapter.notifyDataSetChanged();
            }
        }
        //why, this is dumb... but its not triggering isEmpty lol
        String emptyString = "";
        if (listNameET.getText().toString().equals(emptyString)) {
            Toast emptyToast = Toast.makeText(this, "please enter a name for the list", Toast.LENGTH_LONG);
            emptyToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            emptyToast.show();
            empty = true;
        }
        if (!duplicate && !empty) {
            firstList newList = new firstList(listNameET.getText().toString());
            theList.add(newList);
            firstListView.setAdapter(itemAdapter);
            listNameET.setText("");
            updateTextBasedOnSize();
            itemAdapter.notifyDataSetChanged();
            updateFile();
            //change visibility
            Button addButton = findViewById(R.id.addButton);
            listNameET.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            itemAdapter.setMostRecentlyClickedPosition(-1);
            itemAdapter.notifyDataSetChanged();
        }
    }

    /**
     * method that is executed when the selectButton1 is clicked
     *
     * @param view the view that was clicked (button)
     *             using an explicit intent this brings us to our secondary screen.
     *             This also sends an extra ArrayList<String> theListString, which is a
     *             copy of theList, as well as sending the mostRecentlyClickedPosition.
     */
    public void selectButton1(View view) {
        String helper;
        int mostRecentlyClickedPosition = itemAdapter.getMostRecentlyClickedPosition();
        Intent selectButton1Intent = new Intent(MainActivity.this, SecondaryActivity.class);
        theListString.clear();
        for (int i = 0; i < theList.size(); i++) {
            helper = theList.get(i).getNameOfList();
            theListString.add(i, helper);
        }
        itemAdapter.setParentName(theListString.get(mostRecentlyClickedPosition));
        String parentName = itemAdapter.getParentName();
        selectButton1Intent.putExtra("theListString", theListString);
        selectButton1Intent.putExtra("mostRecentlyClickedPosition", mostRecentlyClickedPosition);
        selectButton1Intent.putExtra("parentName", parentName);
        startActivity(selectButton1Intent);
    }

    /**
     * method that is executed when the hideButton1 is clicked
     *
     * @param view the view that was clicked (button)
     *             using the itemAdapter (+listener) this easily hides the buttons
     */
    public void hideButton1(View view) {
        //makes keyboard go away ~magical incantation~ (in case it's up)
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        itemAdapter.setMostRecentlyClickedPosition(-1);
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * method that is executed when the deleteButton2 is clicked
     *
     * @param view the view that was clicked (button)
     *             removes the corresponding list in theList and updates the file
     *             This also deletes the corresponding file in the secondary section,
     *             ie. all of this list's elements.
     */
    public void deleteButton1(View view) {
        String fileName = "listItems" + theList.get(itemAdapter.getMostRecentlyClickedPosition()).getNameOfList() + ".txt";
        deleteFile(fileName);
        theList.remove(itemAdapter.getMostRecentlyClickedPosition());
        updateTextBasedOnSize();
        itemAdapter.notifyDataSetChanged();
        updateFile();
        itemAdapter.setMostRecentlyClickedPosition(-1);
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * method that is executed when the moveButton1 is clicked
     *
     * @param view the view that was clicked (button)
     *             using Collections.swap(theList, i, j) where i is the index of one item
     *             to be swapped, and j is the index of the other element. Check for validity
     *             of user input, then swap.
     */
    public void moveButton1(View view) {
        //makes keyboard go away ~magical incantation~
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        LinearLayout first_list_layout = (LinearLayout) view.getParent();
        EditText positionEditText1 = first_list_layout.findViewById(R.id.positionEditText1);
        int moveToPosition = -1;
        try {
            //attempt to get the integer which will be assigned to moveToPosition
            //this can throw a NumberFormatException that's why it's in a try-catch block
            moveToPosition = Integer.parseInt(positionEditText1.getText().toString());
        } catch (NumberFormatException e) {
            Toast numberFormatExceptionToast = Toast.makeText(this, "problem with input, please enter a valid number in range", Toast.LENGTH_LONG);
            numberFormatExceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            numberFormatExceptionToast.show();
            positionEditText1.setText("");
        }
        //if the user input was out of theList's range or if the input was empty
        if (moveToPosition > theList.size() || moveToPosition < 0 || positionEditText1.getText().toString().isEmpty()) {
            Toast notValidToast = Toast.makeText(this, "please enter a valid number in range.", Toast.LENGTH_LONG);
            notValidToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            notValidToast.show();
            positionEditText1.setText("");
        }
        //yay valid input, let's move.
        else {
            int i = itemAdapter.getMostRecentlyClickedPosition();
            int j = moveToPosition - 1;
            Collections.swap(theList, i, j);
            updateFile();
            positionEditText1.setText("");
            itemAdapter.setMostRecentlyClickedPosition(-1);
            firstListView.clearChoices();
            itemAdapter.notifyDataSetChanged();

        }
    }

    /**
     * method that is executed when called
     * updates the titleTextView1, if our listOfLists is empty it will display
     * "you do not have any lists." otherwise it will simply display
     * "my lists:". This is called often when an item is
     * added to the list or deleted.
     */
    public void updateTextBasedOnSize() {
        String noListText = "you do not have any lists.";
        String yesListText = "my lists:";
        TextView titleTextView = findViewById(R.id.titleTextView);
        if (theList.size() == 0) {
            titleTextView.setText(noListText);
        } else {
            titleTextView.setText(yesListText);
        }
    }

    /**
     * method that is executed when called
     * updates the file that stores all of the information of each list.
     * each parent list is apart of the listOfLists.txt, however they all have
     * their own "listItems(parentList).txt" this allows for easy deletion
     * of lists and is very organized
     */
    private void updateFile() {
        try {
            FileOutputStream fos = this.openFileOutput("listOfLists.txt", Context.MODE_PRIVATE);
            for (int i = 0; i < theList.size(); i++) {
                try {
                    fos.write(theList.get(i).getNameOfList().getBytes());
                    fos.write("\n".getBytes());
                } catch (Exception e) {
                    Toast exceptionToast = Toast.makeText(this, "problem with writing to output file, listOfLists.txt", Toast.LENGTH_SHORT);
                    exceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                    exceptionToast.show();
                }
            }
        } catch (Exception e) {
            Toast exceptionToast = Toast.makeText(this, "problem with updating output file, listOfLists.txt", Toast.LENGTH_LONG);
            exceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            exceptionToast.show();
        }
    }


    /**
     * method that is executed when called
     * reads the file upon creation, etc. using an InputStreamReader and
     * a BufferedReader
     */
    private void readFile() {
        File file = getBaseContext().getFileStreamPath("listOfLists.txt");
        if (file.exists()) {
            try {
                FileInputStream fis = this.openFileInput("listOfLists.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader bR = new BufferedReader(inputStreamReader);
                String theLine = bR.readLine();
                String tempString;
                while (theLine != null) {
                    tempString = theLine;
                    firstList tempItem = new firstList(tempString);
                    theList.add(tempItem);
                    theLine = bR.readLine();
                }
            } catch (Exception e) {
                Toast exceptionToast = Toast.makeText(this, "problem with reading input file, listOfLists.txt", Toast.LENGTH_LONG);
                exceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                exceptionToast.show();
            }
        } else {
            //nothing needed, probably the first time the app was opened.
        }
    }
}