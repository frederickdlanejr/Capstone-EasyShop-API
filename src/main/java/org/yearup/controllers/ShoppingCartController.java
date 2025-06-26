package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller

// maps information from the shopping cart

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")

// only logged in users should have access to these actions
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    // annotation needed
    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao){
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    // GET cart
    @GetMapping
    public ShoppingCart getCart(Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by username
            User user = userDao.getByUserName(userName);
            // find database user by id
            int userId = user.getId();
            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "UNABLE TO RETRIEVE CART", e);
        }
    }

    // add a POST method to add a product to the cart - the url should be
    @PostMapping("/products/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    public void addToCart(@PathVariable int productId, Principal principal){
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();
            shoppingCartDao.addProduct(userId, productId, 1);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "UNABLE TO ADD TO YOUR CART", e);
        }
    }

    // add a PUT method to update an existing product in the cart - the url should be
    @PutMapping
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    public void updateCartItem(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();
            shoppingCartDao.updateQuantity(userId, productId, item.getQuantity());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "UNABLE TO UPDATE THE CART", e);
        }
    }

    // add a DELETE method to clear all products from the current users cart
    @DeleteMapping
    // https://localhost:8080/cart
    public void clearCart(Principal principal){
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();
            shoppingCartDao.clearShoppingCart(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "UNABLE TO CLEAR THE CART", e);
        }
    }
}