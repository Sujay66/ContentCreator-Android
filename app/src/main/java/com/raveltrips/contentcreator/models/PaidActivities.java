package com.raveltrips.contentcreator.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarasim on 5/28/2017.
 */

public class PaidActivities implements Serializable {


	private String id;
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
	public PaidActivities() {
		this.tags = new ArrayList<>();
		this.imageUrls = new ArrayList<>();
		this.readyForReview = false;
		this.accuracy = true;
	}


	public PaidActivities(String name, String website, String contact, String desc,GPSCoOrdinate gps) {
		super();
		this.name = name;
		this.website = website;
		this.contactnumber = contact;
		this.description = desc;
		this.gps = gps;
	}
	
	public PaidActivities copyFromPaidActivities(PaidActivities paidActivities){
		if(!IsEmpty(paidActivities.getName()))this.name = paidActivities.getName();
		if(!IsEmpty(paidActivities.getWebsite()))this.website = paidActivities.getWebsite();
		if(!IsEmpty(paidActivities.getContactnumber()))this.contactnumber = paidActivities.getContactnumber();
		if(!IsEmpty(paidActivities.getDescription()))this.description = paidActivities.getDescription();
		if(paidActivities.getImageUrls()!=null)this.imageUrls = paidActivities.getImageUrls();
		if(paidActivities.getTags()!=null)this.tags = paidActivities.getTags();
		if(paidActivities.getGps()!=null)this.gps = paidActivities.getGps();
		if(!IsEmpty(paidActivities.getCreatedDate()))this.createdDate = paidActivities.getCreatedDate();
		this.readyForReview = paidActivities.readyForReview;
		this.accuracy = paidActivities.accuracy;
		
			return this;		
	}
	
	boolean IsEmpty(String str){
		return (str == null || str.isEmpty());
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}
	
	/**
	 * @param website the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
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
