package com.ucm.design1;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class InteractiveContactArrayAdapter extends ArrayAdapter<Contact> {

	private List<Contact> contacts = null;
	private Activity context = null;

	public InteractiveContactArrayAdapter(Activity context,
			List<Contact> contacts) {
		super(context, R.layout.rowlayout, contacts);
		this.context = context;
		this.contacts = contacts;

	}

	static class ViewHolder {
		protected TextView number;
		protected TextView name;
		protected CheckBox checkbox;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.rowlayout, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.number = (TextView) view
					.findViewById(R.id.contactNumber);
			viewHolder.name = (TextView) view.findViewById(R.id.contactName);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							Contact element = (Contact) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(contacts.get(position));
		}
		else
		{
			view=convertView;
			((ViewHolder)view.getTag()).checkbox.setTag(contacts.get(position));
		}
		ViewHolder holder=(ViewHolder)view.getTag();
		holder.number.setText(contacts.get(position).number);
		holder.name.setText(contacts.get(position).name);
		holder.checkbox.setChecked(contacts.get(position).isSelected());
		return view;
	}
	
	 

}
