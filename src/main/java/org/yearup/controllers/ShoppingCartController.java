package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller

// maps information from the shopping cart

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")


public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao){
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sowwy :C");
        }
    }

    @PostMapping("/products/{productId}")
    @ResponseStatus(value = HttpStatus.CREATED)

    public ShoppingCart addToCart(@PathVariable int productId, Principal principal) {
        try {

            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            Product product = productDao.getById(productId);

            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
            }

            shoppingCartDao.addItem(userId, productId, 1);

            return shoppingCartDao.getByUserId(userId);

        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sowwy! :C");
        }
    }



    @PutMapping
    public ShoppingCart updateCartItem(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            shoppingCartDao.updateQuantity(userId, productId, item.getQuantity());

            return shoppingCartDao.getByUserId(userId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Sowwy :C");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    @DeleteMapping
    public ShoppingCart clearCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.delete(userId);

            System.out.println("Be gone with this!");

            return new ShoppingCart();

        } catch (Exception e) {
            System.err.println("Error clearing cart: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}