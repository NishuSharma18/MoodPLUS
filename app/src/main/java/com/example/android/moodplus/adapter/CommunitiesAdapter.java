package com.example.android.moodplus.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moodplus.R;
import com.example.android.moodplus.model.Community;

import java.util.ArrayList;

public class CommunitiesAdapter extends
        RecyclerView.Adapter<CommunitiesAdapter.CommunitiesHolder>{

    private ArrayList<Community> communities;
    private Context context;
    private OnCommunityClickListener onCommunityClickListener;

    public CommunitiesAdapter(ArrayList<Community> communities, Context context, OnCommunityClickListener onCommunityClickListener){
        this.communities = communities;
        this.context = context;
        this.onCommunityClickListener = onCommunityClickListener;
    }

    public interface OnCommunityClickListener{
        void onCommunityClicked(int position);
    }



    //Inflating pet_holder xml file.
    @Override
    public CommunitiesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.communities_holder,parent,false);
        return new CommunitiesHolder(view);
    }

    //Binding data to view holder items.
    @Override
    public void onBindViewHolder(CommunitiesHolder holder,int position) {

        holder.Community_name.setText(communities.get(position).getCommName());
        holder.Community_desc.setText(communities.get(position).getCommDesc());
//        holder.Community_pic.setImageDrawable(images.get(position));

    }

    //Returns the number of items in the list.
    @Override
    public int getItemCount() {
        return communities.size();
    }

    //Setting up holder.
    class CommunitiesHolder extends RecyclerView.ViewHolder{

        TextView Community_name;
        ImageView Community_pic;
        TextView Community_desc;

        public CommunitiesHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCommunityClickListener.onCommunityClicked(getAdapterPosition());
                }
            });

            Community_name = itemView.findViewById(R.id.commHolder_name);
            Community_pic = itemView.findViewById(R.id.commHolder_img);
            Community_desc = itemView.findViewById(R.id.commHolder_desc);

        }
    }

}