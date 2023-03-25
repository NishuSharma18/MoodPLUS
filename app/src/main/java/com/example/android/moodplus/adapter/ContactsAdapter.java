package com.example.android.moodplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.android.moodplus.model.MyContact;
import com.example.android.moodplus.R;

import java.util.ArrayList;

public class ContactsAdapter extends
        RecyclerView.Adapter<ContactsAdapter.MyContactHolder> implements Filterable {

    private ArrayList<MyContact> contactsListBackup;
    private ArrayList<MyContact> contact;
    private Context context;
    private onContactsClickListener onContactsClickListener;

    public ContactsAdapter(ArrayList<MyContact> contacts, Context context, onContactsClickListener onContactClickListener){
        this.contact = contacts;
        this.context = context;
        this.onContactsClickListener = onContactClickListener;
        contactsListBackup = new ArrayList<>(contacts);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MyContact> filteredContacts = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredContacts.addAll(contactsListBackup);
            }
            else {
                for (MyContact cont : contactsListBackup){
                    if(cont.getName().toLowerCase().trim().contains(constraint.toString().toLowerCase())){
                        filteredContacts.add(cont);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredContacts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contact.clear();
            contact.addAll((ArrayList<MyContact>)results.values);
            notifyDataSetChanged();
        }
    };


    public interface onContactsClickListener{
        void onContactsClicked(int position);
    }

    //Inflating pet_holder xml file.
    @Override
    public MyContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contacts_holder,parent,false);
        return new MyContactHolder(view);
    }

    //Binding data to view holder items.
    @Override
    public void onBindViewHolder(MyContactHolder holder,int position) {

        holder.Name.setText(contact.get(position).getName());
        holder.Number.setText(contact.get(position).getNumber());

    }

    //Returns the number of items in the list.
    @Override
    public int getItemCount() {
        return contact.size();
    }

    //Setting up holder.
    class MyContactHolder extends RecyclerView.ViewHolder{

        TextView Name;
        TextView Number;
        private ContactsAdapter contactsAdapter;

        public MyContactHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onContactsClickListener.onContactsClicked(getAdapterPosition());

                }
            });

            Name = itemView.findViewById(R.id.contact_name);
            Number = itemView.findViewById(R.id.contact_num);

        }
    }

}