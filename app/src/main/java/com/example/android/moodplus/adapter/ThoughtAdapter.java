package com.example.android.moodplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moodplus.R;
import com.example.android.moodplus.model.Thoughts;

import java.util.ArrayList;

public class ThoughtAdapter extends

        RecyclerView.Adapter<ThoughtAdapter.ThoughtHolder> {

    private ArrayList<Thoughts> thoughts;
    private Context context;

    public ThoughtAdapter(ArrayList<Thoughts> thoughts,Context context) {
        this.thoughts = thoughts;
        this.context = context;
    }

    //Inflating thought_holder xml file.
    @Override
    public ThoughtAdapter.ThoughtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.thought_holder,parent,false);
        return new ThoughtAdapter.ThoughtHolder(view);
    }

    //Binding data to view holder items.
    @Override
    public void onBindViewHolder(ThoughtAdapter.ThoughtHolder holder, int position) {

        holder.txtmsg.setText(thoughts.get(position).getThought());
        holder.txtTime.setText(thoughts.get(position).getTime());

    }

    //Returns the number of items in the list.
    @Override
    public int getItemCount() {
        return thoughts.size();
    }

    //Setting up holder.
    class ThoughtHolder extends RecyclerView.ViewHolder{

        TextView txtmsg;
        TextView txtTime;

        public ThoughtHolder(View itemView){
            super(itemView);

            txtmsg = itemView.findViewById(R.id.thought_msg_tv);
            txtTime = itemView.findViewById(R.id.thought_chat_sender_name_tv);
        }
    }
}