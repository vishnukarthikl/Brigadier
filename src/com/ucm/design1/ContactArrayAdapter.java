package com.ucm.design1;

import java.util.List;

import com.ucm.design1.InteractiveContactArrayAdapter.ViewHolder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {
	private List<Contact> contacts = null;
	private Activity context = null;
	public ContactArrayAdapter(Activity context,
			List<Contact> contacts) {
		super(context, R.layout.blockedrowlayout, contacts);
		this.context = context;
		this.contacts = contacts;
	}
	

	static class ViewHolder {
		protected TextView number;
		protected TextView name;
	
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.blockedrowlayout, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.number = (TextView) view
					.findViewById(R.id.contactNumber);
			viewHolder.name = (TextView) view.findViewById(R.id.contactName);
			view.setTag(viewHolder);
			
		}
		else
		{
			view=convertView;
			
		}
		ViewHolder holder=(ViewHolder)view.getTag();
		holder.number.setText(contacts.get(position).number);
		holder.name.setText(contacts.get(position).name);
		
		return view;
	}
}
