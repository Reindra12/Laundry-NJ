package com.example.enjelaundry;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class TesActivity extends AppCompatActivity {
    BarChart chart;
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.madmin);
        barchart();
    }


    public void barchart(){
        chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp = new ArrayList();
        NoOfEmp.add(new BarEntry(1000, 0));
        NoOfEmp.add(new BarEntry(5000,1));
        NoOfEmp.add(new BarEntry(3000,2));
        NoOfEmp.add(new BarEntry(8000,3));
        NoOfEmp.add(new BarEntry(2000,4));
        NoOfEmp.add(new BarEntry(4000,5));
        NoOfEmp.add(new BarEntry(9000,6));
        NoOfEmp.add(new BarEntry(5000,7));
        NoOfEmp.add(new BarEntry(7000,8));
        NoOfEmp.add(new BarEntry(4000,9));
        NoOfEmp.add(new BarEntry(3000,10));
        NoOfEmp.add(new BarEntry(1000,11));
        ArrayList year = new ArrayList();

        year.add("Januari");
        year.add("Februari");
        year.add("Maret");
        year.add("April");
        year.add("Mei");
        year.add("Juni");
        year.add("Juli");
        year.add("Agustus");
        year.add("September");
        year.add("Oktober");
        year.add("November");
        year.add("Desember");
        BarDataSet bardataset = new BarDataSet(NoOfEmp, "Bulan");
        chart.animateY(5000);
        BarData data = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
    }
}