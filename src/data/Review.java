package data;

public class Review {

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Review(String authorName, String date, String rating, String body) {
		super();

		this.authorName = authorName;
		this.date = date;
		this.rating = rating;
		this.body = body;
	}

	public Review() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Review [ authorName=" + authorName + ", date=" + date
				+ ", rating=" + rating + ", body=" + body + "]";
	}

	String authorName;
	String date;
	String rating;
	String body;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((authorName == null) ? 0 : authorName.hashCode());
		result = (prime * result) + ((body == null) ? 0 : body.hashCode());
		result = (prime * result) + ((date == null) ? 0 : date.hashCode());
		result = (prime * result) + ((rating == null) ? 0 : rating.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Review other = (Review) obj;
		if (authorName == null) {
			if (other.authorName != null) {
				return false;
			}
		} else if (!authorName.equals(other.authorName)) {
			return false;
		}
		if (body == null) {
			if (other.body != null) {
				return false;
			}
		} else if (!body.equals(other.body)) {
			return false;
		}
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (rating == null) {
			if (other.rating != null) {
				return false;
			}
		} else if (!rating.equals(other.rating)) {
			return false;
		}
		return true;
	}

}