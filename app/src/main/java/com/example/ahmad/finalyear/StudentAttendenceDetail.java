package com.example.ahmad.finalyear;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StudentAttendenceDetail extends Activity {
    String [] date,status,lec_name;
    String percentage,section_name,student_id,student_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_summary);
        ListView lv =(ListView) findViewById(R.id.StudentSummary);
        TextView percen=(TextView) findViewById(R.id.percentage);
        TextView sec_name=(TextView) findViewById(R.id.section);
        TextView std_id=(TextView) findViewById(R.id.rgno);
        TextView std_name=(TextView) findViewById(R.id.name);
        Bundle bn = getIntent().getExtras();

        if (bn != null) {
            date = bn.getStringArray("std_date");
            status = bn.getStringArray("std_status");
            lec_name=bn.getStringArray("lec_name");
            section_name=bn.getString("sec_name");
            student_id=bn.getString("id");
            student_name=bn.getString("name");
     }
        sec_name.setText("[" + section_name+"]");
        std_id.setText(student_id);
        std_name.setText(student_name);
        float count=0;
        for (int i=0;i<status.length;i++)
        {
            String zero="0";
            if (status[i].equals(zero))
            {
                count++;
            }
        }

            float total;
        total=  status.length;
        for (int i=0;i<status.length;i++)
        {
            if(status[i].equals("0")) {
                status[i]="-";
            }
            else
            {
                status[i]="A";
            }
        }

        List<String> srnoadd = new ArrayList<String>();
        for (int i=0;i<status.length;i++) {
            srnoadd.add(Integer.toString(i+1));
        }
        String[] srno = new String[ srnoadd.size()];
        srnoadd.toArray(srno);


        SummaryAdapter obj=new SummaryAdapter(StudentAttendenceDetail.this,lec_name,srno,date,status);
        lv.setAdapter(obj);
        percentage=Float.toString((count/total)*100);
        percen.setText("Pecentage: "+percentage+"% ");
    }
}
