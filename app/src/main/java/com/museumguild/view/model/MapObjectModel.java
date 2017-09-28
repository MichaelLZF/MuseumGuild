package com.museumguild.view.model;

import android.location.Location;

import com.museumguild.bean.LineSpot;
import com.museumguild.entity.collection.Collection;

public class MapObjectModel 
{
	private Collection collection;
	private LineSpot lineSpot;
	private int x = 0 ;
	private int y = 0;
	private String id;
	private String caption;
	private Location location;
	private int drawableId;
	private int scala;
	
	public MapObjectModel(String id, Location location, String caption)
	{
		this.location = location;
		this.caption = caption;
		this.id = id;
	}
	
	public MapObjectModel(String id, int x, int y, String caption, int drawableId, int scala)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.caption = caption;
		this.drawableId = drawableId;
		this.scala = scala;
	}

	public MapObjectModel(String id, int x, int y, Collection collection, int drawableId, int scala)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.caption = collection.name;
		this.collection = collection;
		this.drawableId = drawableId;
		this.scala = scala;
	}

	public String getId()
	{
		return id;
	}

	
	public int getX() 
	{
		return x;
	}


	public int getY() 
	{
		return y;
	}
	
	
	public Location getLocation()
	{
		return location;
	}
	
	
	public String getCaption()
	{
		return caption;
	}
	
	public int getDrawableId()
	{
		return drawableId;
	}

	public int getScala() {
		return scala;
	}

	public Collection getCollection() {
		return collection;
	}
}
