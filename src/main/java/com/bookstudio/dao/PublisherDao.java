package com.bookstudio.dao;

import java.util.List;

import com.bookstudio.models.Publisher;

public interface PublisherDao extends CrudDao<Publisher, String> {
	public List<Publisher> populatePublisherSelect();
}
