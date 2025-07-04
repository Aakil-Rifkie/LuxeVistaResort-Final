package com.example.luxevistaresortfinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.List;
public class RoomAdapter extends BaseAdapter{
    private Context context;
    private List<Room> roomList;
    private LayoutInflater inflater;

    int userId;

    public RoomAdapter(Context context, List<Room> roomList, int userId){
        this.context = context;
        this.roomList = roomList;
        this.userId = userId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return roomList.size();
    }

    @Override
    public Object getItem(int position){
        return roomList.get(position);
    }

    @Override
    public long getItemId(int position){
        return roomList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Room room = roomList.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.room_list_item, parent, false);
        }

        ImageView roomImage = convertView.findViewById(R.id.roomImageDisplay);
        TextView roomName = convertView.findViewById(R.id.roomNameDisplay);
        TextView roomDescription = convertView.findViewById(R.id.roomDescriptionDisplay);
        TextView roomPrice = convertView.findViewById(R.id.roomPriceDisplay);
        Button bookRoomButton = convertView.findViewById(R.id.bookRoomButtonDisplay);

        roomName.setText(room.roomName);
        roomDescription.setText(room.description);
        roomPrice.setText("Price: $" + room.price);
        roomImage.setImageResource(R.drawable.roomimage);

        bookRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookingConfirmationActivity.class);
                intent.putExtra("room_id", room.id);
                intent.putExtra("room_name", room.roomName);
                intent.putExtra("room_description", room.description);
                intent.putExtra("room_price", room.price);
                intent.putExtra("user_id", userId);
                context.startActivity(intent);
            }


        });

        return convertView;
    }
}
