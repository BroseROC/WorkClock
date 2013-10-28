package com.broseadventures.workclock;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.broseadventures.lib.data.ValuesMap;
import com.broseadventures.lib.db.DataRow;
import com.broseadventures.lib.db.DataTable;

public class DatabaseService {
    private final static String DATABASE_NAME = "WorkClock.db";
    private final static String TAG = "WorkClock::DatabaseService";

    private EasyDb db;

    public DatabaseService(Context context) {
        Log.d(TAG, "Entering Contructor(Context)");
        this.db = new EasyDb(context, DATABASE_NAME);
        Log.d(TAG, "Leaving Contructor(Context)");
    }

    public void AddSession(Session session) {
        Log.d(TAG, "Entering AddSession");
        long milisElapsed = session.getElapsedTime();
        ValuesMap cv = new ValuesMap();
        cv.Add("JobName", session.getJob().getName());
        cv.Add("PunchIn", session.getPunchIn().getTime());
        cv.Add("PunchOut", session.getPunchOut().getTime());
        cv.Add("Elapsed", milisElapsed);
        Log.i(TAG, "Adding session: " + cv.toString());
        db.Command("Session", cv.GetAll());
        Log.d(TAG, "Leaving AddSession");
    }

    public void AddJob(Job job) {
        Log.d(TAG, "Entering AddJob");
        ValuesMap cv = new ValuesMap();
        cv.Add("Name", job.getName());
        cv.Add("PayRate", job.getHourlyRate());
        Log.i(TAG, "Adding job " + job);
        db.Command("Job", cv.GetAll());
        Log.d(TAG, "Leaving AddJob");
    }

    public void RemoveSession(Session session) {
        Log.d(TAG, "Entering RemoveSession");
        ValuesMap where = new ValuesMap();
        where.Add("SessionId", session.getId());
        db.Delete("Session", where);
        Log.d(TAG, "Leaving RemoveSession");
    }

    public void RemoveJob(Job job) {
        Log.d(TAG, "Entering RemoveJob");
        ValuesMap where = new ValuesMap();
        where.Add("Name", job.getName());
        db.Delete("Job", where);
        Log.d(TAG, "Leaving RemoveJob");
    }

    public Job GetJob(String jobName) {
        Log.d(TAG, "Entering GetJob(" + jobName + ")");
        String sql = "SELECT j.Name, j.PayRate FROM Job AS j WHERE j.Name = '" + jobName + "'";
        this.db.GetDataRow(sql);
        Log.d(TAG, "Leaving GetJob(" + jobName + ")");
        return null;
    }

    public boolean HasJobs() {
        Log.d(TAG, "Entering HasJobs");
        String query = "SELECT COUNT(*) FROM Job";
        int count = this.db.GetInt(query, 0);
        boolean r;
        if (count > 0) {
            r = true;
        } else {
            r = false;
        }
        Log.i(TAG, "HasJobs: " + r);
        Log.d(TAG, "Leaving HasJobs");
        return r;
    }

    public Job[] GetAllJobs() {
        Log.d(TAG, "Entering GetAllJobs");
        DataTable table = this.db.GetDataTable("SELECT Name, PayRate FROM Job");
        ArrayList<Job> list = new ArrayList<Job>(table.RowCount());
        for (DataRow row : table.GetRows()) {
            list.add(new Job(row.GetString("Name"), row.GetDouble("PayRate")));
        }
        Log.d(TAG, "Leaving GetAllJobs");
        return list.toArray(new Job[list.size()]);
    }

    public Session[] GetAllSessions() {
        Log.d(TAG, "Entering GetAllSessions");
        String columns = "Job.PayRate, Job.Name, Session.SessionId, Session.PunchIn, Session.PunchOut";
        String query = "SELECT " + columns + " FROM Session INNER JOIN Job on Job.Name = Session.JobName";
        Log.d(TAG, query);
        DataTable table;
        try {
            table = this.db.GetDataTable(query);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
            return new Session[0];
        }
        ArrayList<Session> sessions = new ArrayList<Session>(table.RowCount());
        for (DataRow row : table.GetRows()) {
            try {
                Job tempJob = new Job(row.GetString("Name"), row.GetDouble("PayRate"));
                sessions.add(new Session(row.GetDate("PunchIn"), row.GetDate("PunchOut"), tempJob));
            } catch (ParseException ex) {
                Log.e(TAG, "GetAllSessions: Error contructing Session " + ex.getMessage());
                continue;
            }
        }
        Log.d(TAG, "Leaving GetAllSessions");
        return sessions.toArray(new Session[sessions.size()]);
    }
}
