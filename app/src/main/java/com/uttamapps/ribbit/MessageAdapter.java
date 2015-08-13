package com.uttamapps.ribbit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Uttam Kumaran on 8/12/2015.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {
    /*
     This adapter will be attached to a ListView and then call getView,
      Inflate it into the layout, then attach it into the ListView */

    protected Context mContext;
    protected List<ParseObject> mMessages;

    public MessageAdapter(Context context, List<ParseObject> messages) {
       super(context, R.layout.message_item, messages);

        mContext = context;
        mMessages = messages;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // need to inflate view - convertView - then return it to the list
        ViewHolder holder;

        if(convertView==null) { //If we are creating view for the first time. Allows use of recycling the view
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null); //LayoutInflater takes XML layouts and turns them into Views in code that we can use
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon); //get it for the layout from this view since we are not attached to the activity yet
            holder.nameLabel = (TextView) convertView.findViewById(R.id.senderLabel);
        }
        else{
            holder = (ViewHolder)convertView.getTag(); //gets ViewHolder that was already created
        }

        ParseObject message = mMessages.get(position);

        if(message.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)){
            holder.iconImageView.setImageResource(R.drawable.ic_action_picture);
        }

        else{
            holder.iconImageView.setImageResource(R.drawable.ic_action_play_over_video);
                    }
        holder.nameLabel.setText(message.getString(ParseConstants.KEY_SENDER_NAME));

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView; //public by default
        TextView nameLabel;

    }
}
