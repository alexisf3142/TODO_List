package com.example.todolist;

public class secondList {

    private String nameOfItem;
    private String nameOfParentList;
    private int indexInList;

    //constructor
    public secondList(String n) {
        this.nameOfItem = n;
    }

    //some not used in this project, however still good to have setters and getters
    //if anyone was ever to continue my code

    //getters
    public String getNameOfItem() {
        return nameOfItem;
    }

    public String getNameOfParentList() {
        return nameOfParentList;
    }
    public int getIndexInList() {
        return indexInList;
    }

    //setters
    public void setNameOfItem(String nameOfItem) {
        this.nameOfItem = nameOfItem;
    }

    public void setNameOfParentList(String nameOfParentList) {
        this.nameOfParentList = nameOfParentList;
    }

    public void setIndexInList(int indexInList) {
        this.indexInList = indexInList;
    }
}

