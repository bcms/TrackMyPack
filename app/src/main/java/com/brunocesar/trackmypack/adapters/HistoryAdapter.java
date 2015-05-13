package com.brunocesar.trackmypack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brunocesar.trackmypack.R;
import com.brunocesar.trackmypack.models.History;

import java.util.List;

/**
 * Created by BrunoCesar on 05/05/2015.
 */
public class HistoryAdapter extends ArrayAdapter<History> {
    private final Context context;
    private final List<History> values;

    public HistoryAdapter(Context context, List<History> values) {
        super(context, R.layout.adapter_history_row, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.adapter_history_row, parent, false);
        TextView placeTextView = (TextView) rowView.findViewById(R.id.place_text_view);
        TextView actionTextView = (TextView) rowView.findViewById(R.id.action_text_view);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.date_text_view);
        //TextView codeTextView = (TextView) rowView.findViewById(R.id.code_text_view);

        placeTextView.setText(values.get(position).getPlace());
        actionTextView.setText(values.get(position).getAction());
        dateTextView.setText(values.get(position).getDate());

        return rowView;
    }
}
