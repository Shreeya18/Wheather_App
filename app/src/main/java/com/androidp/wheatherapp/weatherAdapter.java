package com.androidp.wheatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class weatherAdapter extends RecyclerView.Adapter<weatherAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherModal> weatherModalArrayList;

    public weatherAdapter(Context context, ArrayList<WeatherModal> weatherModalArrayList) {
        this.context = context;
        this.weatherModalArrayList = weatherModalArrayList;
    }

    @NonNull
    @Override
    public weatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull weatherAdapter.ViewHolder holder, int position) {

        WeatherModal modal = weatherModalArrayList.get(position);
        holder.idTemperature.setText(modal.getTemperature());
        holder.wind.setText(modal.getWindSpeed()+"km/hr");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        SimpleDateFormat output =  new SimpleDateFormat("hh:min aa");
        try {
            Date t = input.parse(modal.getTime());
            holder.idTime.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }

        Picasso.get().load("https:".concat(modal.getIcon())).into(holder.idCondition);

    }

    @Override
    public int getItemCount() {
        return weatherModalArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView idTemperature, idTime, wind;
        private ImageView idCondition;

        public ViewHolder(@NonNull View itemView) {


            super(itemView);

            idCondition = itemView.findViewById(R.id.idCondition);
            idTemperature = itemView.findViewById(R.id.idTemperature);
            wind = itemView.findViewById(R.id.idTextview);
            idTime = itemView.findViewById(R.id.idTime);


        }
    }
}
