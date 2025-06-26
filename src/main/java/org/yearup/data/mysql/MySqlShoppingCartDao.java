package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        String query = """
                SELECT
                product_id, quantity
                FROM
                shopping_card
                WHERE
                user_id = ?
                """;

        ShoppingCart cart = new ShoppingCart();


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
