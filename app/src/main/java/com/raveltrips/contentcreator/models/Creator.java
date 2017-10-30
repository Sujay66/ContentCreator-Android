/* Copyright 2017 - www.raveltrips.com
* You may not use this file except in compliance with the License.
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.raveltrips.contentcreator.models;

import java.util.List;

/**
 * @author Akash Anjanappa (akash.a2351@gmail.com)
 * 09-Apr-2017
 */
public class Creator {

	private String id;
	private String name;
	private String email;
	private String imageUrl;
	private String description;
	private Integer viewCount;
	private Integer reviewCount;
	private String createdDate;
	private String modifiedDate;
	private String quickBio;
	private String youtubeURL;
	private List<String> tripsCompletedIds;
	private List<String> tripsWishlistIds;
	private List<String> orderIds;

	public Creator() {}
	
	public Creator(String name, String email, String description, Integer viewCount, Integer reviewCount,
			String createdDate, String modifiedDate,String bio, String youtube,List<String> tripsWishlistIds, List<String> tripsCompletedIds, List<String> orderIds) {
		super();
		this.name = name;
		this.email = email;
		this.description = description;
		this.viewCount = viewCount;
		this.reviewCount = reviewCount;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.quickBio = bio;
		this.youtubeURL = youtube;
		this.tripsCompletedIds = tripsCompletedIds;
		this.tripsWishlistIds = tripsWishlistIds;
		this.orderIds = orderIds;
	}

	public Creator copyFromCreator(Creator creator){
		if(!IsEmpty(creator.getName()))this.name = creator.getName();
		if(!IsEmpty(creator.getEmail()))this.email = creator.getEmail();
		if(!IsEmpty(creator.getDescription()))this.description = creator.getDescription();
		if(!IsEmpty(creator.getCreatedDate()))this.createdDate = creator.getCreatedDate();
		if(!IsEmpty(creator.getImageUrl()))this.imageUrl = creator.getImageUrl();
		if(!IsEmpty(creator.getQuickBio()))this.quickBio = creator.getQuickBio();
		if(!IsEmpty(creator.getYoutubeURL()))this.youtubeURL = creator.getYoutubeURL();
		if(creator.getTripsCompletedIds()!=null)this.setTripsCompletedIds(creator.getTripsCompletedIds());
		if(creator.getTripsWishlistIds()!=null)this.setTripsWishlistIds(creator.getTripsWishlistIds());
		if(creator.getOrderIds()!=null)this.setOrderIds(creator.getOrderIds());
		setReviewCount(creator.getReviewCount());
		setViewCount(creator.getViewCount());
		return this;
	}
	
	boolean IsEmpty(String str){
		return (str == null || str.isEmpty());
	}
	
	
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the viewCount
	 */
	public Integer getViewCount() {
		return viewCount;
	}

	/**
	 * @param viewCount the viewCount to set
	 */
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the quickBio
	 */
	public String getQuickBio() {
		return quickBio;
	}

	/**
	 * @param bio the quickBio to set
	 */
	public void getQuickBio(String bio) {
		this.quickBio = bio;
	}

	/**
	 * @return the youtubeURL
	 */
	public String getYoutubeURL() {
		return youtubeURL;
	}

	/**
	 * @param url the youtubeURL to set
	 */
	public void setYoutubeURL(String url) {
		this.youtubeURL = url;
	}

	/**
	 * @return the tripsCompletedIds
	 */
	public List<String> getTripsCompletedIds() {
		return tripsCompletedIds;
	}

	/**
	 * @param tripsCompletedIds the tripsCompletedIds to set
	 */
	public void setTripsCompletedIds(List<String> tripsCompletedIds) {
		this.tripsCompletedIds = tripsCompletedIds;
	}

	/**
	 * @return the tripsWishlistIds
	 */
	public List<String> getTripsWishlistIds() {
		return tripsWishlistIds;
	}

	/**
	 * @param tripsWishlistIds the tripsWishlistIds to set
	 */
	public void setTripsWishlistIds(List<String> tripsWishlistIds) {
		this.tripsWishlistIds = tripsWishlistIds;
	}

	/**
	 * @return the orderIds
	 */
	public List<String> getOrderIds() {
		return orderIds;
	}

	/**
	 * @param orderIds the orderIds to set
	 */
	public void setOrderIds(List<String> orderIds) {
		this.orderIds = orderIds;
	}

}
