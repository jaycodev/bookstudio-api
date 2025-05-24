package com.bookstudio.models;

import com.bookstudio.utils.IdFormatter;

public class Course {
	private String courseId;
	private String formattedCourseId;
	private String name;
	private String level;
	private String description;
	private String status;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
