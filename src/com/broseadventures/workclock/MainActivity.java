package com.broseadventures.workclock;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    private static final String TAG = "WorkClock::MainActivity";

    Job currentJob;
    DatabaseService db;
    boolean punchedIn;
    Date punchInTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(Bundle.EMPTY);
        Log.d(TAG, "Entering onCreate");
        this.setContentView(R.layout.activity_main);
        this.db = new DatabaseService(this.getApplicationContext());
        this.checkJobs();
        Log.d(TAG, "Leaving onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "Entering onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.main, menu);
        Log.d(TAG, "Leaving onCreateOptionsMenu");
        return true;

    }

    /**
     * Enables the punch in and punch out buttons if jobs exist and disables
     * them if no jobs exists
     */
    private void checkJobs() {
        if (this.db.HasJobs()) {
            this.findViewById(R.id.btnPunchIn).setEnabled(true);
            this.findViewById(R.id.btnPunchOut).setEnabled(true);
        } else {
            this.findViewById(R.id.btnPunchIn).setEnabled(false);
            this.findViewById(R.id.btnPunchOut).setEnabled(false);
        }
    }

    public void onAddJobClick(View v) {
        Log.d(TAG, "Entering onAddJobClick");
        AlertDialog.Builder addJobBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflate = this.getLayoutInflater();
        // Final so it can be referenced in the dialog's onClick function
        final View dialogView = inflate.inflate(R.layout.add_job_dialog, null);
        addJobBuilder.setMessage(R.string.add_job_button);
        addJobBuilder.setView(dialogView);
        addJobBuilder.setCancelable(true);
        addJobBuilder.setNegativeButton("Cancel", null);
        addJobBuilder.setPositiveButton("Add", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "In positive.onclick");
                EditText name = (EditText) dialogView.findViewById(R.id.jobName);
                String jobName = name.getText().toString();
                EditText rate = (EditText) dialogView.findViewById(R.id.jobRate);
                Double jobRate = Double.valueOf(rate.getText().toString());
                Job tempNewJob = new Job(jobName, jobRate);
                MainActivity.this.db.AddJob(tempNewJob);
                MainActivity.this.currentJob = tempNewJob;
                MainActivity.this.checkJobs();
            }
        });
        AlertDialog ad = addJobBuilder.create();
        ad.show();
        Log.d(TAG, "Leaving onAddJobClick");
    }

    public void onPunchInClick(View v) {
        Log.d(TAG, "Entering onPunchInClick");
        this.punchInTime = new Date();
        this.punchedIn = true;
        this.findViewById(R.id.btnPunchOut).setClickable(true);
        Log.d(TAG, "Leaving onPunchInClick");
    }

    public void onPunchOutClick(View v) {
        Log.d(TAG, "Entering onPunchOutClick");
        Date punchOutTime = new Date();
        Session temp = new Session(this.punchInTime, punchOutTime, this.currentJob);
        this.db.AddSession(temp);
        this.punchedIn = false;
        this.findViewById(R.id.btnPunchOut).setClickable(false);
        Log.d(TAG, "Leaving onPunchOutClick");
    }

    public void onViewSessionsClick(View v) {
        Log.d(TAG, "Entering onViewSessionsClick");
        Session[] sessions = this.db.GetAllSessions();
        for (int i = 0; i < sessions.length; i++) {
            Log.d(TAG, sessions[i].toString());
        }
        Intent i = new Intent(this, ViewSessions.class);
        i.putExtra("sessions", sessions);
        startActivity(i);
        Log.d(TAG, "Leaving onViewSessionsClick");
    }
}
