package com.brunocesar.trackmypack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brunocesar.trackmypack.R;
import com.brunocesar.trackmypack.models.Package;

import java.util.List;

/**
 * Created by BrunoCesar on 18/04/2015.
 */
public class PackageAdapter extends ArrayAdapter<com.brunocesar.trackmypack.models.Package> {
    private final Context context;
    private final List<Package> values;

    public PackageAdapter(Context context, List<Package> values) {
        super(context, R.layout.adapter_package_row, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.adapter_package_row, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.name_text_view);
        TextView codeTextView = (TextView) rowView.findViewById(R.id.code_text_view);

        nameTextView.setText(values.get(position).getName());
        codeTextView.setText(values.get(position).getCode());

        return rowView;
    }
}
