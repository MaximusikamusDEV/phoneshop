package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartItem;
import com.es.core.cart.CartService;
import com.es.core.cart.exceptions.OutOfStockException;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.controller.forms.BulkCartForm;
import com.es.phoneshop.web.controller.forms.BulkCartItemForm;
import com.es.phoneshop.web.controller.mappers.BulkCartFormMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/bulkCart")
public class BulkCartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private BulkCartFormMapper bulkCartFormMapper;
    @Resource
    private Validator validator;

    @GetMapping
    public String getBulkCart(Model model) {
        BulkCartForm bulkCartForm = new BulkCartForm();
        initializeBulkCartForm(bulkCartForm);
        populateBulkCartModel(model, bulkCartForm);
        return "bulkCart";
    }

    @PostMapping(value = "/addToCart")
    public String addToCart(@ModelAttribute(WebConstants.BULK_CART_FORM_ATTR) BulkCartForm bulkCartForm,
                            BindingResult bindingResult,
                            Model model) {
        deleteEmptyRows(bulkCartForm);

        validator.validate(bulkCartForm, bindingResult);

        if (bindingResult.hasErrors()) {
            populateBulkCartModel(model, bulkCartForm);
            return "bulkCart";
        }

        List<CartItem> cartItems = bulkCartFormMapper.convertToCartItems(bulkCartForm);

        try {
            cartItems.forEach(cartItem ->
                    cartService.addPhone(cartItem.getPhone().getId(), cartItem.getQuantity())
            );
        } catch (OutOfStockException e) {
            throw new RuntimeException(e);
        }

        populateBulkCartModel(model, bulkCartForm);
        return "redirect:/bulkCart";
    }

    private void deleteEmptyRows(BulkCartForm bulkCartForm) {
        bulkCartForm.getItems().removeIf(item ->
                (StringUtils.isEmpty(item.getPhoneBrand()) &&
                        StringUtils.isEmpty(item.getPhoneModel()) &&
                        item.getQuantity() == null));
    }

    private void populateBulkCartModel(Model model, BulkCartForm bulkCartForm) {
        Cart cart = cartService.getCart();
        model.addAttribute(WebConstants.BULK_CART_FORM_ATTR, bulkCartForm);
        model.addAttribute(WebConstants.CART_COST_ATTR, cart.getTotalCost());
        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cart.getTotalQuantity());
    }

    private void initializeBulkCartForm(BulkCartForm bulkCartForm) {
        ArrayList<BulkCartItemForm> itemsForm = (ArrayList<BulkCartItemForm>) IntStream.range(0, WebConstants.ROWS_AMOUNT_IN_BULK_CART)
                .mapToObj(i -> new BulkCartItemForm())
                .collect(Collectors.toList());

        bulkCartForm.setItems(itemsForm);
    }
}
