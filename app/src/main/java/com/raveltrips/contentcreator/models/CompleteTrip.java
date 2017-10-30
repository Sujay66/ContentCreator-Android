/* Copyright 2017 - www.raveltrips.com
* You may not use this file except in compliance with the License.
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.raveltrips.contentcreator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akash Anjanappa (akash.a2351@gmail.com)
 * 16-May-2017
 */
public class CompleteTrip {

	private String id;
	private String name;
	private String description;
	private String location;
	private GPSCoOrdinate gps;
	private List<String> videoUrls;
	private List<String> imageUrls;
	private Double price;
	private String length;
	private Double rating;
	private List<Review> reviews;

	public String getTripLength() {
		return length;
	}

	public void setTripLength(String tripLength) {
		this.length = tripLength;
	}

	private Integer reviewCount;
	private List<String> keywords;
	private List<Pindrop> pindrops;
	private String createdDate;
	private String modifiedDate;
	private String creatorId;
	private boolean readyForReview;
	private boolean visibleToPublic;
	private List<Restaurants> restaurants;
	private List<PaidActivities> paidActivities;
	private List<String> tags;


	public Boolean isNull()
	{

		if (this.name!=null && this.description!=null && this.location!=null && this.videoUrls!=null
				&& this.price!=null && !isRestaurantsEmpty() && !isEmptyPaidActivities() && !isGemsEmpty())
		{
            return false;
		}
		else {
            return true;
        }



	}
	public CompleteTrip(){
		this.name = "UnknownTrip";
		this.gps = new GPSCoOrdinate();
		this.paidActivities = new ArrayList<PaidActivities>();
		this.restaurants = new ArrayList<Restaurants>();
		this.imageUrls = new ArrayList<>();
		this.keywords = new ArrayList<>();
		this.pindrops = new ArrayList<>();
		this.videoUrls = new ArrayList<>();
		this.tags = new ArrayList<>();
		this.price = 2.0;
		this.rating = 2.0;
		this.reviews = new ArrayList<>();
		this.reviewCount=0;

	}
	
	public CompleteTrip copyFromTrip(CompleteTrip trip){
		if(!IsEmpty(trip.getId()))this.id = trip.getId();
		if(!IsEmpty(trip.getTripLength()))this.id = trip.getId();
		if(!IsEmpty(trip.getName()))this.name = trip.getName();
		if(!IsEmpty(trip.getDescription()))this.description = trip.getDescription();
		if(trip.getVideoUrls()!=null)this.videoUrls = trip.getVideoUrls();
		if(trip.getImageUrls()!=null)this.imageUrls = trip.getImageUrls();
		if(trip.gettags()!=null)this.tags = trip.gettags();
		if(trip.getPrice()>0)this.price = trip.getPrice();
		if(trip.getRating()>0)this.rating = trip.getRating();
		if(trip.getReviews()!=null){
			this.reviews = trip.getReviews();
			this.reviewCount = reviews.size();
		}
		if(trip.getKeywords()!=null)this.keywords = trip.getKeywords();
		if(!IsEmpty(trip.getCreatedDate()))this.createdDate = trip.getCreatedDate();
		if(!IsEmpty(trip.getCreatorId()))this.creatorId = trip.getCreatorId();
		if(!IsEmpty(trip.getLocation()))this.location = trip.getLocation();
		if(trip.getGps()!=null)this.gps = trip.getGps();
		if(trip.isReadyForReview())this.readyForReview = true; else this.readyForReview = false;
		if(trip.isVisibleToPublic())this.visibleToPublic = true; else this.visibleToPublic = false;
		if(trip.getRestaurants()!=null){
			this.restaurants = trip.getRestaurants();
		}
		
		return this;
	}
	
	boolean IsEmpty(String str){
		return (str == null || str.isEmpty());
	}
	
	
	/**
	 * @return the readyForReview
	 */
	public boolean isReadyForReview() {
		return readyForReview;
	}

	/**
	 * @param readyForReview the readyForReview to set
	 */
	public void setReadyForReview(boolean readyForReview) {
		this.readyForReview = readyForReview;
	}

	/**
	 * @return the visibleToPublic
	 */
	public boolean isVisibleToPublic() {
		return visibleToPublic;
	}

	/**
	 * @param visibleToPublic the visibleToPublic to set
	 */
	public void setVisibleToPublic(boolean visibleToPublic) {
		this.visibleToPublic = visibleToPublic;
	}

	/**
	 * @return the restaurants
	 */
	public List<Restaurants> getRestaurants() {
		return restaurants;
	}

	/**
	 * @param restaurants the restaurants to set
	 */
	public void setRestaurants(List<Restaurants> restaurants) {
		this.restaurants = restaurants;
	}



	/**
	 * @return the paidActivities
	 */
	public List<PaidActivities> getPaidActivities() {
		return paidActivities;
	}

	/**
	 * @param paidActivities the paidActivities to set
	 */
	public void setPaidActivities(List<PaidActivities> paidActivities) {
		this.paidActivities = paidActivities;
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
	 * @return the videoUrls
	 */
	public List<String> getVideoUrls() {
		return videoUrls;
	}
	/**
	 * @param videoUrls the videoUrls to set
	 */
	public void setVideoUrls(List<String> videoUrls) {
		this.videoUrls = videoUrls;
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
	 * @return the reviewCount
	 */
	public Integer getReviewCount() {
		return reviewCount;
	}
	/**
	 * @param reviewCount the reviewCount to set
	 */
	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}
	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the pindrops
	 */
	public List<Pindrop> getPindrops() {
		return pindrops;
	}
	/**
	 * @param pindrops the pindrops to set
	 */
	public void setPindrops(List<Pindrop> pindrops) {
		this.pindrops = pindrops;
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

	public void settags(List<String> creatorId) {
		this.tags = creatorId;
	}

	public List<String> gettags() {
		return tags;
	}
    public boolean isRestaurantsEmpty() {
        for(Restaurants restaurants : this.getRestaurants()){
            if(restaurants.getName()!=null && restaurants.getContactnumber()!=null && restaurants.getDescription()!=null
                    && restaurants.getTags()!=null && restaurants.getImageUrls()!=null  ){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }
    public Boolean isEmptyPaidActivities()
    {
        for(PaidActivities paidActivities : this.getPaidActivities()){
            if(paidActivities.getName()!=null && paidActivities.getContactnumber()!=null && paidActivities.getDescription()!=null
                    && paidActivities.getTags()!=null && paidActivities.getImageUrls()!=null  ){
                return false;
            }
            else{
                return true;
            }
        }
        return true;
    }

    public boolean isGemsEmpty() {
        for(Pindrop pindrops : this.getPindrops()){
            if(pindrops.getName()!=null && pindrops.getGps()!=null&& pindrops.getDescription()!=null
                    && pindrops.getTags()!=null && pindrops.getImageUrls()!=null  ){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }

//    public boolean isPindDropEmpty() {
//        for(Pindrop pindrop : this.getPindrops()){
//            if(pindrop.getName()!=null && pindrop.get!=null && pindrop.getDescription()!=null
//                    && pindrop.getTags()!=null && pindrop.getImageUrls()!=null  ){
//                return false;
//            }else{
//                return true;
//            }
//        }
//        return false;
//    }
}
