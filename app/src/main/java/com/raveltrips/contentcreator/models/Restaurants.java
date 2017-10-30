package com.raveltrips.contentcreator.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sujay Mahesh (sujay@raveltrips.com)
 * 29-May-2017
 */
public class Restaurants implements Serializable{

	private String name;
	private String website;
	private String contactnumber;
	private List<String> tags;
	private String description;
	private List<String> imageUrls;
	private String createdDate;
	private String modifiedDate;
	private GPSCoOrdinate gps;
	private boolean readyForReview;
	private boolean accuracy;

	public Restaurants() {
		this.readyForReview = false;
		this.accuracy = true;
		this.tags = new ArrayList<>();
		this.imageUrls = new ArrayList<>();
	}



	public Boolean isNull()
	{
		if (this.name != null && this.contactnumber!=null && this.description!= null && tags!=null && imageUrls!=null)
			return false;
		else
			return true;
	}

	/**
	 * @param name,website,contactnumber,description
	 */
	public Restaurants(String name, String website, String contact, String desc, GPSCoOrdinate gps) {
		super();
		this.name = name;
		this.website = website;
		this.contactnumber = contact;
		this.description = desc;
		this.gps = gps;
	}
	
	public Restaurants copyFromRestaurant(Restaurants restaurant){
		if(!IsEmpty(restaurant.getName()))this.name = restaurant.getName();
		if(!IsEmpty(restaurant.getWebsite()))this.website = restaurant.getWebsite();
		if(!IsEmpty(restaurant.getContactNumber()))this.contactnumber = restaurant.getContactNumber();
		if(!IsEmpty(restaurant.getDescription()))this.description = restaurant.getDescription();
		if(restaurant.getImageUrls()!=null)this.imageUrls = restaurant.getImageUrls();
		if(restaurant.getTags()!=null)this.tags = restaurant.getTags();
		if(restaurant.getGps()!=null)this.gps = restaurant.getGps();
		if(!IsEmpty(restaurant.getCreatedDate()))this.createdDate = restaurant.getCreatedDate();
		this.readyForReview = restaurant.readyForReview;
		this.accuracy = restaurant.accuracy;
		
			return this;		
	}
	
	boolean IsEmpty(String str){
		return (str == null || str.isEmpty());
	}
	
	/**
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	

	/**
	 * @param name the name to set
	 */
	public void setName(String name){
		this.name = name;
	}
	
	
	/**
	 * @return the contactnumber
	 */
	public String getContactnumber() {
		return contactnumber;
	}

	/**
	 * @param contactnumber the contactnumber to set
	 */
	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the website
	 */
	public String getWebsite(){
		return website;
	}
	

	/**
	 * @param website the Website to set
	 */
	public void setWebsite(String website){
		this.website = website;
	}
	
	
	/**
	 * @return the contactnumber
	 */
	public String getContactNumber(){
		return contactnumber;
	}
	


	public void setContactNumber(String ContactNumber){
		this.contactnumber = ContactNumber;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the imageUrls
	 */
	public List<String> getImageUrls() {
		return imageUrls;
	}


	/**
	 * @param imageUrls the imageUrls to set
	 */
	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}


	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the gps
	 */
	public GPSCoOrdinate getGps() {
		return gps;
	}

	/**
	 * @param gps the gps to set
	 */
	public void setGps(GPSCoOrdinate gps) {
		this.gps = gps;
	}
	/**
	 * @param ready the readyForReview to set
	 */
	public void setreadyForReview(Boolean ready) {
		this.readyForReview = ready;
	}

	/**
	 * @return the readyForReview
	 */
	public boolean getreadyForReview () {
		return readyForReview;
	}

	/**
	 * @param acc the accuracy to set
	 */
	public void setaccuracy(Boolean acc) {
		this.accuracy = acc;
	}

	/**
	 * @return the accuracy
	 */
	public boolean getaccuracy () {
		return accuracy;
	}
}
