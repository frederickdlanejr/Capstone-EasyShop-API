package org.yearup.data.mysql;

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
             PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, userId);

            try (ResultSet results = ps.executeQuery()){
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
        }catch (SQLException e ) {
            throw new RuntimeException("Error getting shopping cart for user " + userId, e);
        }
        return cart;
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {
        String checkQuery = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String insertQuery = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateQuery = "UPDATE shopping_cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection()) {
            try (PreparedStatement checkPs = connection.prepareStatement(checkQuery)){
                checkPs.setInt(1, userId);
                checkPs.setInt(2, productId);

                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updatePs = connection.prepareStatement(updateQuery)){
                            updatePs.setInt(1, quantity);
                            updatePs.setInt(2, userId);
                            updatePs.setInt(3, productId);
                            updatePs.executeUpdate();

                        }
                    }else {
                        try (PreparedStatement insertPs = connection.prepareStatement(insertQuery)) {
                            insertPs.setInt(1, userId);
                            insertPs.setInt(2, productId);
                            insertPs.setInt(3, quantity);
                            insertPs.executeUpdate();

                        }
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Error adding item to card", e);
        }
    }

    @Override
    public void updateItem(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            removeItem(userId, productId);
            return;
        }

        String query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException("Error updating cart item", e);
        }
    }

    @Override
    public void removeItem(int userId, int productId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException("Error removing cart item", e);
        }
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

    @Override
    public void delete(int userId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, userId);
            ps.executeUpdate();

        }catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }

    @Override
    public void save(ShoppingCart cart) {
        throw new UnsupportedOperationException("Use addItem/updateItem/removeItem instead");
    }
}