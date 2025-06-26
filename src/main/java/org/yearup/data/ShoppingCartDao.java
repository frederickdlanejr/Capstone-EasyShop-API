package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void save(ShoppingCart cart);
    void delete(int userId);
    void addItem(int userId, int productId, int quantity);
    void updateItem(int userId, int productId, int quantity);
    void removeItem(int userId, int productId);
    void updateQuantity(int userId, int productId, int quantity);
    void clearShoppingCart(int userId);
    void addProduct(int userId, int productId, int i);
    // add additional method signatures here
}
