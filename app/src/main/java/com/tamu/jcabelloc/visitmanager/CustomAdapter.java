package com.tamu.jcabelloc.visitmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jcabelloc on 2/10/2018.
 */

public class CustomAdapter extends ArrayAdapter<VisitTask> {
    public CustomAdapter(@NonNull Context context, ArrayList<VisitTask> visitTasks) {
        super(context, 0, visitTasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        VisitTask visitTask = getItem(position);
        if (convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_visit_task, parent, false);
        }
        TextView nameItemTextView = (TextView) convertView.findViewById(R.id.nameItemTextView);
        TextView reasonItemTextView = (TextView) convertView.findViewById(R.id.reasonItemTextView);
        ImageButton  locationItemImageButton = (ImageButton) convertView.findViewById(R.id.locationItemImageButton);
        nameItemTextView.setText(visitTask.getAgent());
        reasonItemTextView.setText(visitTask.getReason());
        return  convertView;
        //return super.getView(position, convertView, parent);
    }
}
