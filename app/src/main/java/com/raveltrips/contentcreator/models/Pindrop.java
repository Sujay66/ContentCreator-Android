/* Copyright 2017 - www.raveltrips.com
* You may not use this file except in compliance with the License.
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.raveltrips.contentcreator.models;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Akash Anjanappa (akash.a2351@gmail.com)
 * 09-Apr-2017
 */
public class Pindrop implements Serializable{


	private String id;
	private String name;
	private String location;
	private String description;
	private Double rating;
	private List<Review> reviews;
	private List<String> imageUrls;
	private Double price;
	private GPSCoOrdinate gps;
	private String createdDate;
	private String modifiedDate;
	private String creatorId;
	private List<String> tags;
	private String latitude;
	private String longitude;
	private boolean readyForReview;
	private boolean accuracy;
	
	
	public Pindrop(){
		this.tags = new ArrayList<>();
		this.imageUrls = new ArrayList<>();
        this.readyForReview = false;
        this.accuracy = true;
	}

	public Pindrop(String name, String location, Double rating, List<Review> reviews, Double price, GPSCoOrdinate gps,
			String createdDate, String creatorId) {
		super();
		this.name = name;
		this.location = location;
		this.rating = rating;
		this.reviews = reviews;
		this.price = price;
		this.gps = gps;
		this.createdDate = createdDate;
		this.creatorId = creatorId;
	}

	protected Pindrop(Parcel in) {
		id = in.readString();
		name = in.readString();
		location = in.readString();
		description = in.readString();
		imageUrls = in.createStringArrayList();
		createdDate = in.readString();
		modifiedDate = in.readString();
		creatorId = in.readString();
		tags = in.createStringArrayList();
		latitude = in.readString();
		longitude = in.readString();
	}

//	public static final Creator<Pindrop> CREATOR = new Creator<Pindrop>() {
//		@Override
//		public Pindrop createFromParcel(Parcel in) {
//			return new Pindrop(in);
//		}
//
//		@Override
//		public Pindrop[] newArray(int size) {
//			return new Pindrop[size];
//		}
//	};

	public Pindrop copyFromPindrop(Pindrop pindrop){
		if(!IsEmpty(pindrop.getName()))this.name = pindrop.getName();
		if(!IsEmpty(pindrop.getLocation()))this.location = pindrop.getLocation();
		if(!IsEmpty(pindrop.getDescription()))this.description = pindrop.getDescription();
		if(pindrop.getRating()>0)this.rating = pindrop.getRating();
		if(pindrop.getPrice()>0)this.price = pindrop.getPrice();
		if(pindrop.getImageUrls()!=null)this.imageUrls = pindrop.getImageUrls();
		if(pindrop.getReviews()!=null)this.reviews = pindrop.getReviews();
		if(pindrop.getGps()!=null)this.gps = pindrop.getGps();
		if(!IsEmpty(pindrop.getCreatedDate()))this.createdDate = pindrop.getCreatedDate();
		if(!IsEmpty(pindrop.getCreatorId()))this.creatorId = pindrop.getCreatorId();
		if(pindrop.getTags()!=null)this.tags = pindrop.getTags();
		if(pindrop.getLatitude()!=null)this.latitude = pindrop.getLatitude();
		if(pindrop.getLongitude()!=null)this.latitude = pindrop.getLongitude();
		this.readyForReview = pindrop.readyForReview;
		this.accuracy = pindrop.accuracy;
		
		return this;
		
	}
	
	boolean IsEmpty(String str){
		return (str == null || str.isEmpty());
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
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the creatorId
	 */
	public String getCreatorId() {
		return creatorId;
	}


	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
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
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the rating
	 */
	public Double getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Double rating) {
		this.rating = rating;
	}



	/**
	 * @return the reviews
	 */
	public List<Review> getReviews() {
		return reviews;
	}


	/**
	 * @param reviews the reviews to set
	 */
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}


	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
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

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	public void setLatitude(String lat){
		this.latitude = lat;
	}

	public void setLongitude(String lon){
		this.longitude = lon;
	}
	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(id);
//		dest.writeString(name);
//		dest.writeString(location);
//		dest.writeString(description);
//		dest.writeStringList(imageUrls);
//		dest.writeString(createdDate);
//		dest.writeString(modifiedDate);
//		dest.writeString(creatorId);
//		dest.writeStringList(tags);
//		dest.writeString(latitude);
//		dest.writeString(longitude);
//	}
}
