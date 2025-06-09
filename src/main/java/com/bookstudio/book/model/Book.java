package com.bookstudio.book.model;

import java.time.LocalDate;

import com.bookstudio.shared.util.IdFormatter;

public class Book {
	private String bookId;
	private String formattedBookId;
	private String title;
	private int totalCopies;
	private int availableCopies;
	private int loanedCopies;
	private String authorId;
	private String formattedAuthorId;
	private String authorName;
	private String publisherId;
	private String formattedPublisherId;
	private String publisherName;
	private String courseId;
	private String formattedCourseId;
	private String courseName;
	private LocalDate releaseDate;
	private String genreId;
	private String genreName;
	private String status;

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
        this.formattedBookId = IdFormatter.formatId(bookId, "L");
	}

	public String getFormattedBookId() {
		return formattedBookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(int totalCopies) {
		this.totalCopies = totalCopies;
	}

	public int getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(int availableCopies) {
		this.availableCopies = availableCopies;
	}

	public int getLoanedCopies() {
		return loanedCopies;
	}

	public void setLoanedCopies(int loanedCopies) {
		this.loanedCopies = loanedCopies;
	}

	public String getAuthorId() {
		return authorId;
	}

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
        this.formattedAuthorId = IdFormatter.formatId(authorId, "A");
    }
    
	public String getFormattedAuthorId() {
		return formattedAuthorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
        this.formattedPublisherId = IdFormatter.formatId(publisherId, "ED");
	}
	
	public String getFormattedPublisherId() {
		return formattedPublisherId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
        this.formattedCourseId = IdFormatter.formatId(courseId, "C");
	}
	
	public String getFormattedCourseId() {
		return formattedCourseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getGenreId() {
		return genreId;
	}

	public void setGenreId(String genreId) {
		this.genreId = genreId;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
