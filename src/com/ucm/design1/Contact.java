package com.ucm.design1;

public class Contact {
	public String id;
	public String name;
	public String number;
	private boolean selected;
	public boolean isSynced;
	
	
	public Contact(String id, String name, String number) {
		this.id=id;
		this.name=name;
		this.number=number;
		this.isSynced=true;
	}
	public Contact()
	{
		this.id=null;
		this.name=null;
		this.number=null;
		this.isSynced=false;
	}
	
	public Contact(String name,String number)
	{
		this.name=name;
		this.number=number;
		isSynced=false;
	}

	public Contact(Contact toAddcontact) {
		this.id=toAddcontact.id;
		this.isSynced=toAddcontact.isSynced;
		this.name=toAddcontact.name;
		this.number=toAddcontact.number;
	}
	public void putValue(int i, String value) {
		switch (i) {
		case 0:
			id = value;
			break;
		case 1:
			name = value;
			break;
		case 2:
			number = value;
			break;

		default:
			break;
		}
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
