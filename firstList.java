package com.example.todolist;

public class firstList {

    private String nameOfList;
    public int indexInList;

    //constructor
    public firstList(String n) {
        this.nameOfList = n;
    }

    //some not used in this project, however still good to have setters and getters
    //if anyone was ever to continue my code

    //getters
    public String getNameOfList() {
        return nameOfList;
    }

    public int getIndexInList() {
        return indexInList;
    }

    //setters
    public void setNameOfList(String nameOfList) {
        this.nameOfList = nameOfList;
    }

    public void setIndexInList(int indexInList) {
        this.indexInList = indexInList;
    }
}
