package com.example.ahmad.finalyear;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class SummaryAdapter extends BaseAdapter {
    String [] date_str,status_str,srno_str,lec_name;
    Context context;
    private static LayoutInflater inflater=null;
    public SummaryAdapter(StudentAttendenceDetail std_attd,String[] lect_name,String[] srno,String[] date,String[] status) {
        // TODO Auto-generated constructor stub
        lec_name=lect_name;
        srno_str=srno;
        date_str=date;
        status_str=status;
        context=std_attd;


        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return status_str.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public class Holder
    {
        TextView srno;
        TextView lecname;
        TextView date;
        TextView status;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        view = inflater.inflate(R.layout.summary_adapter, null);

        holder.srno=(TextView) view.findViewById(R.id.srno);
        holder.lecname=(TextView) view.findViewById(R.id.lecname);
        holder.date=(TextView) view.findViewById(R.id.date);
        holder.status=(TextView) view.findViewById(R.id.status);
        holder.srno.setText(srno_str[position]);
        holder.lecname.setText(lec_name[position]);
        holder.date.setText(date_str[position]);
        holder.status.setText(status_str[position]);

        return view;
    }



}
