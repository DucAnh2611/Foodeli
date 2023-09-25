package com.example.foodeli.Activities.Auth.Signup;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodeli.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatePickerCustom {

    private Spinner yearSpinner, monthSpinner, daySpinner;
    private ArrayAdapter<String> yearAdapter, monthAdapter, dayAdapter;
    private int currentYear, currentMonth, currentDay;
    private int selectYear, selectMonth, selectDay;
    private Context context;

    public DatePickerCustom(Spinner daySpinner, Spinner monthSpinner, Spinner yearSpinner, Context context) {
        this.daySpinner = daySpinner;
        this.monthSpinner = monthSpinner;
        this.yearSpinner = yearSpinner;
        this.context = context;
    }

    public void setup() {
        yearAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        monthAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        dayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());

        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        yearSpinner.setAdapter(yearAdapter);
        monthSpinner.setAdapter(monthAdapter);
        daySpinner.setAdapter(dayAdapter);

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DATE);

        daySpinner.setSelection(0);
        monthSpinner.setSelection(0);
        yearSpinner.setSelection(0);

        initYearData(currentYear);
        updateMonthData(currentYear);
        updateDayData(currentYear, currentMonth);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedYear = currentYear - position; // Calculate selected year based on position

                selectYear = selectedYear;
                selectMonth = 1;
                selectDay = 1;

                updateMonthData(selectedYear);
                updateDayData(selectedYear, currentMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedYear = currentYear - yearSpinner.getSelectedItemPosition();
                int selectedMonth = position + 1; // Add 1 since it returns zero-based value

                // Update day spinner's data based on selected year and month
                selectMonth = selectedMonth;
                selectDay = 1;
                updateDayData(selectedYear, selectedMonth);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedDay = position + 1;
                selectDay = selectedDay ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initYearData(int currentYear) {
        List<String> years = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            years.add(String.valueOf(currentYear - i));
        }

        yearAdapter.clear();
        yearAdapter.addAll(years);
        yearAdapter.notifyDataSetChanged();
    }
    private void updateMonthData(int selectedYear) {
        List<String> months = new ArrayList<>();

        if (selectedYear == currentYear) {
            for (int i = 1; i <= currentMonth; i++) {
                months.add(String.valueOf(i));
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                months.add(String.valueOf(i));
            }
        }

        monthSpinner.setSelection(0);
        monthAdapter.clear();
        monthAdapter.addAll(months);
        monthAdapter.notifyDataSetChanged();
    }
    private void updateDayData(int selectedYear, int selectedMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, 1);

        int maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<String> days = new ArrayList<>();

        if (selectedYear == currentYear && selectedMonth == currentMonth) {
            for (int i = 1; i <= Math.min(currentDay,maxDaysInMonth); i++) {
                days.add(String.valueOf(i));
            }
        }else{
            for (int i = 1; i <= maxDaysInMonth; i++) {
                days.add(String.valueOf(i));
            }
        }

        daySpinner.setSelection(0);
        dayAdapter.clear();
        dayAdapter.addAll(days);
        dayAdapter.notifyDataSetChanged();
    }

    public int getSelectDay() {
        return selectDay;
    }

    public int getSelectMonth() {
        return selectMonth;
    }

    public int getSelectYear() {
        return selectYear;
    }
}
