package com.example.smart_fridge_android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FoodCustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private List<String> parsedValues;

    private String id;
    private String name;
    private String expirationDate;
    private String expires = " - Expires: ";

    public FoodCustomAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        parsedValues = parse(values.get(position));

        id = parsedValues.get(0);
        name = parsedValues.get(1);
        expirationDate = parsedValues.get(2);

        TextView idView = (TextView) rowView.findViewById(R.id.food_id);
        idView.setText(String.valueOf(id));

        TextView nameView = (TextView) rowView.findViewById(R.id.foodName);
        nameView.setText(name);

        TextView expirationView = (TextView) rowView.findViewById(R.id.foodExpirationDate);

        if (expired(expirationDate)){
            expirationView.setTextColor(Color.RED);
        }

        expirationView.setText(expires + expirationDate);


        return rowView;
    }

    private List<String> parse(String string){

        List<String> list = new ArrayList<String>();

        String[] parsed = string.split(",");

        list.add(parsed[0]);
        list.add(parsed[1]);
        list.add(parsed[2]);

        return list;
    }

    private boolean expired(String expirationDate){
        String[] date;
        int year;
        int month;
        int day;

        if (expirationDate.equals("No Expiration Date Set")){
            return true;
        }

        date = expirationDate.split("/");
        month = Integer.parseInt(date[0]);
        day = Integer.parseInt(date[1]);
        year = Integer.parseInt(date[2]);

        if (year <= Calendar.getInstance().get(Calendar.YEAR)){

            if (month <= Calendar.getInstance().get(Calendar.MONTH) + 1){ // Calendar months are 0 based ... :(

                if (day <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                    return true;
                }
            }
        }

        return false;
    }
}
