package com.example.luxevistaresortfinal;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import java.util.List;

public class ServiceAdapter extends BaseAdapter {

    private Context context;
    private List<Service> serviceList;
    private LayoutInflater inflater;

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return serviceList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Service service = serviceList.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.service_list_item, parent, false);
        }

        TextView serviceName = convertView.findViewById(R.id.serviceNameDisplay);
        TextView serviceDescription = convertView.findViewById(R.id.serviceDescriptionDisplay);
        TextView servicePrice = convertView.findViewById(R.id.servicePriceDisplay);
        Button bookServiceButton = convertView.findViewById(R.id.bookServiceButton);

        serviceName.setText(service.serviceName);
        serviceDescription.setText(service.description);
        servicePrice.setText("Price: $" + service.price);

        bookServiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ServiceBookingConfirmationActivity.class);
            intent.putExtra("service_id", service.id);
            intent.putExtra("service_name", service.serviceName);
            intent.putExtra("service_description", service.description);
            intent.putExtra("service_price", service.price);
            context.startActivity(intent);
        });

        return convertView;
    }
}
