package com.academy.sda.databasetest.model.comments;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;

public class Task implements Parcelable {

    private long id;
    private String description;
    private String category;
    private LocalDate date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.description);
        dest.writeString(this.category);
        dest.writeSerializable(this.date);
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.description = in.readString();
        this.category = in.readString();
        this.date = (LocalDate) in.readSerializable();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
