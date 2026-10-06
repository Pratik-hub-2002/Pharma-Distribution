package com.pratik.pharma.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pratik.pharma.model.Category;
import com.pratik.pharma.util.DBConnection;

public class CategoryDAO {

    public List<Category> getAllCategories() {

        List<Category> categories = new ArrayList<>();

        String sql =
                "SELECT category_id, category_name " +
                "FROM categories " +
                "ORDER BY category_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Category category = new Category();

                category.setCategoryId(
                        rs.getInt("category_id"));

                category.setCategoryName(
                        rs.getString("category_name"));

                categories.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }

    public Category getCategoryById(int categoryId) {

        String sql =
                "SELECT category_id, category_name " +
                "FROM categories " +
                "WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Category category = new Category();

                    category.setCategoryId(
                            rs.getInt("category_id"));

                    category.setCategoryName(
                            rs.getString("category_name"));

                    return category;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}