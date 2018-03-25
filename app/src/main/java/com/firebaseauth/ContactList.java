package com.firebaseauth;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aashka on 25-03-2018.
 */

public class ContactList extends ArrayAdapter<UserContact> {
    private Activity context;
    private List<UserContact> contactList;

    public ContactList(Activity context, List<UserContact> contactList){
        super(context, R.layout.list_layout, contactList);
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textCntName = (TextView) listViewItem.findViewById(R.id.textCntName);
        TextView textCntNum = (TextView) listViewItem.findViewById(R.id.textCntNum);

        UserContact contact = contactList.get(position);

        textCntName.setText(contact.getName());
        textCntNum.setText(contact.getContact());

        return listViewItem;
    }
}
