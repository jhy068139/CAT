package com.example.cat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.cat.database.model.Note;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter
{
    LayoutInflater inflater = null;
    private ArrayList<Note> m_oData = null;
    private int nListCnt = 0;

    public ListAdapter(ArrayList<Note> _oData)
    {
        m_oData = _oData;
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return m_oData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView oTextTitle = (TextView) convertView.findViewById(R.id.textTitle);
        TextView oTextDate = (TextView) convertView.findViewById(R.id.textDate);

        oTextTitle.setText(m_oData.get(position).getTag());
        oTextDate.setText(m_oData.get(position).getTimestamp());
        return convertView;
    }
}

