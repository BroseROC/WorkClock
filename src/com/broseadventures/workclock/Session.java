package com.broseadventures.workclock;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Session implements Parcelable {
    private int id;
    private Date punchIn;
    private Date punchOut;
    private long elapsedTime;
    private Job job;
    public final int ParcelableDesc = 14464;

    public boolean HasJob() {
        return this.job != null;
    }

    public String toString() {
        return "ID:'" + this.id + "', PunchIn:'" + punchIn.toString() + "', PunchOut:'" + punchOut.toString() + "', Elapsed:'"
                + elapsedTime + "', " + job.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getPunchIn() {
        return punchIn;
    }

    public void setPunchIn(Date punchIn) {
        this.punchIn = punchIn;
        this.elapsedTime = punchOut.getTime() - punchIn.getTime();
    }

    public Date getPunchOut() {
        return punchOut;
    }

    public void setPunchOut(Date punchOut) {
        this.punchOut = punchOut;
        this.elapsedTime = punchOut.getTime() - punchIn.getTime();
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Session(Date punchIn, Date punchOut, Job job) {
        super();
        this.punchIn = punchIn;
        this.punchOut = punchOut;
        this.elapsedTime = punchOut.getTime() - punchIn.getTime();
        this.job = job;
    }

    @Override
    public int describeContents() {
        return ParcelableDesc;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.job.getName());
        dest.writeDouble(this.job.getHourlyRate());

        dest.writeLong(this.punchIn.getTime());
        dest.writeLong(this.punchOut.getTime());
    }

    public static final Parcelable.Creator<Session> CREATOR = new Parcelable.Creator<Session>() {
        public Session createFromParcel(Parcel in) {
            Job j = new Job(in.readString(), in.readDouble());
            Date punchIn = new Date(in.readLong());
            Date punchOut = new Date(in.readLong());
            return new Session(punchIn, punchOut, j);
        }

        public Session[] newArray(int size) {
            return new Session[size];
        }
    };
}
