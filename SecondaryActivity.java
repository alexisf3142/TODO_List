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

public class SecondaryActivity extends AppCompatActivity {

    //important variables
    ArrayList<secondList> theList = new ArrayList<secondList>();
    secondListAdapter itemAdapter;
    ListView secondListView;
    String nameParent;

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
        setContentView(R.layout.activity_secondary);
        secondListView = findViewById(R.id.LOLListView2);

        //get the intent that got us here
        Intent callerIntent = getIntent();

        //extract the information from the intent
        ArrayList<String> theListString = callerIntent.getStringArrayListExtra("theListString");
        int mostRecentlyClickedPosition = callerIntent.getIntExtra("mostRecentlyClickedPosition", -1);
        nameParent = callerIntent.getStringExtra("parentName");

        //put the extracted information on the screen
        String helper;
        TextView titleTextView2 = findViewById(R.id.titleTextView2);
        helper = theListString.get(mostRecentlyClickedPosition);
        if (theList.size() != 0) {
            theList.get(mostRecentlyClickedPosition).setNameOfParentList(nameParent);
        }
        titleTextView2.setText(helper.concat(":"));
        //update text on screen such as if the list was empty or not the text is different
        updateTextBasedOnSize();
        //set up adapter + on item click listener
        itemAdapter = new secondListAdapter(this, theList);
        secondListView.setAdapter(itemAdapter);
        secondListView.setOnItemClickListener(listener);
        //read the file and update screen if needed
        readFile();
        updateTextBasedOnSize();
        itemAdapter.notifyDataSetChanged();
        //double check visibility
        EditText listElementET = findViewById(R.id.listElementEditText2);
        Button addButton2 = findViewById(R.id.addButton2);
        listElementET.setVisibility(View.GONE);
        addButton2.setVisibility(View.GONE);
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * method that is executed when the addListButton2 is clicked
     *
     * @param view the view that was clicked (button)
     *             this changes the visibility of the listElementEditText2 & addButton2
     */
    public void addListButton2(View view) {
        EditText elementNameET = findViewById(R.id.listElementEditText2);
        Button addButton2 = findViewById(R.id.addButton2);
        elementNameET.setVisibility(View.VISIBLE);
        addButton2.setVisibility(View.VISIBLE);
    }

    /**
     * method that is executed when the addButton2 is clicked
     *
     * @param view the view that was clicked (button)
     *             takes the user input and add it to theList, and updates the file
     */
    public void addButton2(View view) {
        //makes keyboard go away ~magical incantation~
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //check for validity
        EditText listElementET = findViewById(R.id.listElementEditText2);
        boolean duplicate = false;
        boolean empty = false;
        for (int i = 0; i < theList.size(); i++) {
            if (listElementET.getText().toString().equals(theList.get(i).getNameOfItem())) {
                Toast notValidToast = Toast.makeText(this, "an item of that name already exists in this list.", Toast.LENGTH_LONG);
                notValidToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                notValidToast.show();
                duplicate = true;
                listElementET.setText("");
            }
        }
        //why, this is dumb... but its not triggering isEmpty lol
        String emptyString = "";
        if (listElementET.getText().toString().equals(emptyString)) {
            Toast emptyToast = Toast.makeText(this, "please enter a name for the list", Toast.LENGTH_LONG);
            emptyToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            emptyToast.show();
            empty = true;
        }
        if (!duplicate && !empty) {
            secondList currentList = new secondList(listElementET.getText().toString());
            theList.add(theList.size(), currentList);
            secondListView.setAdapter(itemAdapter);
            listElementET.setText("");
            updateTextBasedOnSize();
            updateFile();
            //change visibility
            Button addButton2 = findViewById(R.id.addButton2);
            listElementET.setVisibility(View.GONE);
            addButton2.setVisibility(View.GONE);
            itemAdapter.notifyDataSetChanged();
            itemAdapter.notifyDataSetChanged();
        }
    }

    /**
     * method that is executed when the hideButton2 is clicked
     *
     * @param view the view that was clicked (button)
     *             using the itemAdapter (+listener) this easily hides the buttons
     */
    public void hideButton2(View view) {
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
     *             removes the corresponding item in theList and updates the file
     */
    public void deleteButton2(View view) {
        theList.remove(itemAdapter.getMostRecentlyClickedPosition());
        updateTextBasedOnSize();
        itemAdapter.notifyDataSetChanged();
        updateFile();
        itemAdapter.setMostRecentlyClickedPosition(-1);
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * method that is executed when the moveButton2 is clicked
     *
     * @param view the view that was clicked (button)
     *             using Collections.swap(theList, i, j) where i is the index of one item
     *             to be swapped, and j is the index of the other element. Check for validity
     *             of user input, then swap.
     */
    public void moveButton2(View view) {
        //makes keyboard go away ~magical incantation~
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        LinearLayout second_list_layout = (LinearLayout) view.getParent();
        EditText positionEditText2 = second_list_layout.findViewById(R.id.positionEditText2);
        int moveToPosition = -1;
        try {
            //attempt to get the integer which will be assigned to moveToPosition
            //this can throw a NumberFormatException that's why it's in a try-catch block
            moveToPosition = Integer.parseInt(positionEditText2.getText().toString());
        } catch (NumberFormatException e) {
            Toast numberFormatExceptionToast = Toast.makeText(this, "problem with input, please enter a valid number in range", Toast.LENGTH_LONG);
            numberFormatExceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            numberFormatExceptionToast.show();
            positionEditText2.setText("");
            itemAdapter.notifyDataSetChanged();
        }
        //if the user input was out of theList's range or if the input was empty
        if (moveToPosition > theList.size() || moveToPosition < 0 || positionEditText2.getText().toString().isEmpty()) {
            Toast notValidToast = Toast.makeText(this, "please enter a valid number in range.", Toast.LENGTH_LONG);
            notValidToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            notValidToast.show();
            positionEditText2.setText("");
        }
        //yay valid input, let's move.
        else {
            int i = itemAdapter.getMostRecentlyClickedPosition();
            int j = moveToPosition - 1;
            Collections.swap(theList, i, j);
            updateFile();
            positionEditText2.setText("");
            itemAdapter.setMostRecentlyClickedPosition(-1);
            secondListView.clearChoices();
            itemAdapter.notifyDataSetChanged();
        }
    }

    /**
     * method that is executed when called
     * updates the titleTextView2, if our list is empty it will display
     * "(name of parent list) is currently empty" otherwise it will display
     * the name of the parent list. This is called often when an item is
     * added to the list or deleted.
     */
    public void updateTextBasedOnSize() {
        String emptyText = " is currently empty.";
        TextView titleTextView2 = findViewById(R.id.titleTextView2);
        if (theList.size() == 0) {
            titleTextView2.setText(nameParent.concat(emptyText));
        } else {
            titleTextView2.setText(nameParent.concat(":"));
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
        String fileName = "listItems" + nameParent + ".txt";
        try {
            FileOutputStream fos = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            for (int i = 0; i < theList.size(); i++) {
                try {
                    fos.write(theList.get(i).getNameOfItem().getBytes());
                    fos.write("\n".getBytes());
                } catch (Exception e) {
                    Toast exceptionToast = Toast.makeText(this, "problem with writing to output file, secondaryList.", Toast.LENGTH_SHORT);
                    exceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                    exceptionToast.show();
                }
            }
        } catch (Exception e) {
            Toast exceptionToast = Toast.makeText(this, "problem with updating output file " + fileName + " , secondaryList.", Toast.LENGTH_LONG);
            exceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
            exceptionToast.show();
        }

    }

    /**
     * method that is executed when called
     * reads the file upon creation, etc. creates fileName for different parentLists
     * when an element is added. Uses an InputStreamReader and a BufferedReader
     */
    private void readFile() {
        String fileName = "listItems" + nameParent + ".txt";
        File file = getBaseContext().getFileStreamPath(fileName);
        if (file.exists()) {
            try {
                FileInputStream fis = this.openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader bR = new BufferedReader(inputStreamReader);
                String theLine = bR.readLine();
                String tempItemName;
                while (theLine != null) {
                    tempItemName = theLine;
                    secondList tempItem = new secondList(tempItemName);
                    theList.add(tempItem);
                    theLine = bR.readLine();
                }
            } catch (Exception e) {
                Toast exceptionToast = Toast.makeText(this, "problem with reading input file " + fileName + " , secondaryList.", Toast.LENGTH_LONG);
                exceptionToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
                exceptionToast.show();
            }
        } else {
            //nothing needed, probably the first time the app was opened.
        }
    }
}