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
    public MySqlShoppingCartDao(DataSource dataSource) {
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

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);

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
            throw new RuntimeException("Error getting shopping cart for user " + userId, e);
        }

        return cart;
    }


    @Override
    public void save(ShoppingCart cart) {

    }

    @Override
    public void delete(int userId) {

    }

    @Override
    public void addItem(int userId, int productId, int quantity) {

    }

    @Override
    public void updateItem(int userId, int productId, int quantity) {

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
