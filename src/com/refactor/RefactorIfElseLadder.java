package com.refactor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class RefactorIfElseLadder {

    //Calculate discount based on user type

    public static void main(String[] args) {
        Cart cart1 = new Cart("NEW", 1000);
        Cart cart2 = new Cart("SILVER", 1000);
        Cart cart3 = new Cart("GOLD", 1000);
        List<Cart> carts = Arrays.asList(cart1, cart2, cart3);

        //Calculate Discount using if else based ladder
        carts.stream().forEach(cart -> {
            System.out.println("For UserType "+cart.getUserType()+" " +
                    "Discount:  "+RefactorIfElseLadder.calculateDiscount(cart));
        });

        //Calculate Discount using functional approach
        carts.stream().forEach(cart -> {
            Object discount = CartRules.getRuleFor(cart).apply(cart);
            System.out.println("For UserType "+cart.getUserType()+" " + "Discount:  "+discount);
        });

    }

    public static double calculateDiscount(Cart cart){
        if(cart.getUserType().equals("SILVER")){
            //15%
            return cart.getBillAmount()*0.15;
        }else if(cart.getUserType().equals("GOLD")){
            //25%
            return cart.getBillAmount()*0.25;
        }else if(cart.getUserType().equals("NEW")){
            //5%
            return cart.getBillAmount()*0.05;
        }else{
            //2%
            return cart.getBillAmount()*0.02;
        }
    }
}

class CartDiscountFunction {
    static Function<Cart, Double> DISCOUNT_15 = cart -> cart.getBillAmount()*0.15;
    static Function<Cart, Double> DISCOUNT_25 = cart -> cart.getBillAmount()*0.25;
    static Function<Cart, Double> DISCOUNT_05 = cart -> cart.getBillAmount()*0.05;
}

class CartPredicateRule{
    static Predicate<Cart> CART_SILVER = cart -> cart.getUserType().equals("SILVER");
    static Predicate<Cart> CART_GOLD = cart -> cart.getUserType().equals("GOLD");
    static Predicate<Cart> CART_NEW = cart -> cart.getUserType().equals("NEW");
}


class CartRules {

    private static Map<Predicate, Function> cartRules = new HashMap<>();

    static {
        cartRules.put(CartPredicateRule.CART_NEW, CartDiscountFunction.DISCOUNT_05);
        cartRules.put(CartPredicateRule.CART_SILVER, CartDiscountFunction.DISCOUNT_15);
        cartRules.put(CartPredicateRule.CART_GOLD, CartDiscountFunction.DISCOUNT_25);
    }

    public static Function getRuleFor(Cart cart){
        return cartRules.entrySet()
                .stream()
                .filter( rule -> rule.getKey().test(cart))
                .map( entry -> entry.getValue())
                .findFirst()
                .get();
    }
}

class Cart {
    private String userType;
    private double billAmount;

    public Cart(String userType, double billAmount) {
        this.userType = userType;
        this.billAmount = billAmount;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }
}

