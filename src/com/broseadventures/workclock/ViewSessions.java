package com.broseadventures.workclock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewSessions extends Activity {

    public String TAG = "WorkClock::ViewSessions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Entering onCreate");
        this.setContentView(R.layout.view_sessions);
        Parcelable[] parcs = this.getIntent().getParcelableArrayExtra("sessions");
        this.sessions = new Session[parcs.length];
        for(int i = 0; i < parcs.length;i++){
            sessions[i] = (Session) parcs[i];
        }
        TableLayout t = (TableLayout) this.getLayoutInflater().inflate(R.layout.view_sessions, null);
        populateTable(t);
        setContentView(t);
        Log.d(TAG, "Leaving onCreate");
    }

    private Session[] sessions;

    private void populateTable(TableLayout table) {
        for (Session s : sessions) {
            TableRow row = new TableRow(this);
            //Name
            TextView v = new TextView(this);
            v.setText(s.getJob().getName());
            row.addView(v);
            //PunchIn
            v = new TextView(this);
            v.setText(s.getPunchIn().toString());
            row.addView(v);
            //PunchOut
            v = new TextView(this);
            v.setText(s.getPunchOut().toString());
            row.addView(v);
            //Elapsed
            v = new TextView(this);
            v.setText(String.valueOf(s.getElapsedTime()));
            row.addView(v);
            
            table.addView(row);
        }
    }

}
