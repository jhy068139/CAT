package com.example.cat.database.model;


public class Note {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TAG = "tag";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_SETTINGTIME = "settingtime";

    private int id;
    private String note;
    private String tag;
    private String timestamp;

    private String settingtime;

    public String getSettingtime() {
        return settingtime;
    }

    public void setSettingtime(String settingtime) {
        this.settingtime = settingtime;
    }





    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TAG + " TEXT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_SETTINGTIME + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Note() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Note(int id, String tag, String note, String settingtime, String timestamp) {
        this.id = id;
        this.tag = tag;
        this.note = note;
        this.settingtime = settingtime;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
