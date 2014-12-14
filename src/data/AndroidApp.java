package data;

import java.util.List;

public class AndroidApp {

	private String id;
	private String category;
	private boolean isFree;
	private String filesize;
	private String creationDate;
	private String currentRating;
	private String description;
	private String developer;
	private List<String> similarApps;
	private String numReviews;
	private String numDownloads;
	private List<Review> reviews;
	private String lastUpdate;
	private String timeStamp;
	private String releaseLogs;
	private List<String> permissions;
	private int numReviewsCollected;



	public String getReleaseLogs() {
		return releaseLogs;
	}

	public void setReleaseLogs(String releaseLogs) {
		this.releaseLogs = releaseLogs;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public AndroidApp(String id, String category, boolean isFree,
			String filesize, String creationDate, String currentRating,
			String description, String developer, List<String> similarApps,
			String numReviews, String numDownloads, List<Review> reviews,
			String lastUpdate, String timeStamp, String releaseLogs,
			List<String> permissions, int numReviewsCollected) {
		super();
		this.id = id;
		this.category = category;
		this.isFree = isFree;
		this.filesize = filesize;
		this.creationDate = creationDate;
		this.currentRating = currentRating;
		this.description = description;
		this.developer = developer;
		this.similarApps = similarApps;
		this.numReviews = numReviews;
		this.numDownloads = numDownloads;
		this.reviews = reviews;
		this.lastUpdate = lastUpdate;
		this.timeStamp = timeStamp;
		this.releaseLogs = releaseLogs;
		this.permissions = permissions;
		this.numReviewsCollected = numReviewsCollected;
	}

	public int getNumReviewsCollected() {
		return numReviewsCollected;
	}

	public void setNumReviewsCollected(int numReviewsCollected) {
		this.numReviewsCollected = numReviewsCollected;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void addNewReview(List<Review> newReviews) {

		for (final Review rev : newReviews) {
			if (!reviews.contains(rev)) {
				reviews.add(rev);

			}
		}
	}

	public void addNewSimilarApss(List<String> newSimilar) {

		for (final String a : newSimilar) {
			if (!similarApps.contains(a)) {
				similarApps.add(a);

			}
		}
	}

	public AndroidApp() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCurrentRating() {
		return currentRating;
	}

	public void setCurrentRating(String currentRating) {
		this.currentRating = currentRating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public List<String> getSimilarApps() {
		return similarApps;
	}

	public void setSimilarApps(List<String> similarApps) {
		this.similarApps = similarApps;
	}

	public String getNumReviews() {
		return numReviews;
	}

	public void setNumReviews(String numReviews) {
		this.numReviews = numReviews;
	}

	public String getNumDownloads() {
		return numDownloads;
	}

	public void setNumDownloads(String numDownloads) {
		this.numDownloads = numDownloads;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public String toString() {
		return "AndroidApp [id=" + id + ", category=" + category + ", isFree="
				+ isFree + ", filesize=" + filesize + ", creationDate="
				+ creationDate + ", currentRating=" + currentRating
				+ ", description=" + description + ", developer=" + developer
				+ ", similarApps=" + similarApps + ", numReviews=" + numReviews
				+ ", numDownloads=" + numDownloads + ", lastUpdate=" + lastUpdate + ", timeStamp=" + timeStamp
				+ ", releaseLogs=" + releaseLogs + ", permissions="
				+ permissions + ", numReviewsCollected=" + numReviewsCollected
				+ "]";
	}




}
