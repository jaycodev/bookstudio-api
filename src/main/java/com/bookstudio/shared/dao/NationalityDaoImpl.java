package com.bookstudio.shared.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bookstudio.shared.model.Nationality;
import com.bookstudio.shared.util.DbConnection;

public class NationalityDaoImpl implements NationalityDao {
	@Override
	public List<Nationality> populateNationalitySelect() {
		List<Nationality> nationalityList = new ArrayList<>();

		String sql = """
				    SELECT NationalityID, NationalityName
				    FROM Nationalities
				""";

		try (Connection cn = DbConnection.getConexion();
				PreparedStatement ps = cn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Nationality nationality = new Nationality();
				nationality.setNationalityId(rs.getString("NationalityID"));
				nationality.setNationalityName(rs.getString("NationalityName"));
				nationalityList.add(nationality);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nationalityList;
	}
}
