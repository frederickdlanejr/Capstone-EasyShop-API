package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private ProductDao productDao;


    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String query = """
                SELECT product_id, quantity
                FROM shopping_cart
                WHERE user_id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            try (ResultSet results = ps.executeQuery()) {
                while (results.next()) {
                    int productId = results.getInt("product_id");
                    int quantity = results.getInt("quantity");

                    Product product = productDao.getById(productId);

                    if (product != null) {
                        ShoppingCartItem item = new ShoppingCartItem();
                        item.setProduct(product);
                        item.setQuantity(quantity);
                        cart.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving shopping cart for user " + userId, e);
        }

        return cart;
    }

    @Override
    public void save(ShoppingCart cart) {

    }

    @Override
    public void delete(int userId) {

    }
//fix this later dummy
    @Override
    public void addItem(int userId, int productId, int quantity) {
        String checkQuery = """
                SELECT quantity
                FROM shopping_cart
                WHERE user_id = ? AND product_id = ?
                """;

        String updateQuery = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?
                """;

        String insertQuery = """
                INSERT INTO shopping_cart (quantity, user_id, product_id)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {

            checkPs.setInt(1, userId);
            checkPs.setInt(2, productId);

            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    // Item exists: update
                    try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {
                        updatePs.setInt(1, quantity);
                        updatePs.setInt(2, userId);
                        updatePs.setInt(3, productId);
                        updatePs.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                        insertPs.setInt(1, userId);
                        insertPs.setInt(2, productId);
                        insertPs.setInt(3, quantity);
                        insertPs.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Sowwy " + userId, e);
        }
    }

    @Override
    public void updateItem(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            removeItem(userId,productId);
            return;
        }

        String query = "Update shopping_cart Set quantity =? WHERE user_id = ? AND product_id = ?";

        try(Connection connection = getConnection();

    }

    @Override
    public void removeItem(int userId, int productId) {

    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity) {

    }

    @Override
    public void clearShoppingCart(int userId) {

    }

    @Override
    public void addProduct(int userId, int productId, int i) {

    }
}
