package com.bookstudio.publisher.dao;

import java.util.List;

import com.bookstudio.publisher.model.Publisher;
import com.bookstudio.shared.dao.CrudDao;

public interface PublisherDao extends CrudDao<Publisher, String> {
	public List<Publisher> populatePublisherSelect();
}
