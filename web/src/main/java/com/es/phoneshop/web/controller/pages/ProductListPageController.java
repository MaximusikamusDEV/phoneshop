package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.core.model.phone.Phone;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.enums.SortField;
import com.es.phoneshop.web.enums.SortOrder;
import com.es.phoneshop.web.exceptions.InvalidPageNumberException;
import com.es.phoneshop.web.services.PhoneService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneService phoneService;
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = WebConstants.BRAND_VALUE) String sortField,
            @RequestParam(required = false, defaultValue = WebConstants.ASC_VALUE) String sortOrder,
            Model model) {
        String validatedQuery = validateQuery(query);
        int totalPages = phoneService.getTotalPageQuantity(validatedQuery);

        validatePage(page, totalPages);
        List<Phone> phones = phoneService.getAllPhones(
                page,
                validatedQuery,
                SortField.valueOfCode(sortField).getCode(),
                SortOrder.valueOfCode(sortOrder).getCode());

        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cartService.getCart().getTotalQuantity());
        model.addAttribute(WebConstants.CART_COST_ATTR, cartService.getCart().getTotalCost());
        model.addAttribute(WebConstants.PHONES_ATTR, phones);
        model.addAttribute(WebConstants.CURR_PAGE_ATTR, page);
        model.addAttribute(WebConstants.TOTAL_PAGES_ATTR, totalPages);
        return "productList";
    }

    private void validatePage(int page, int totalPages) {
        if (totalPages == 0) {
            return;
        }

        if (page < 1 || page > totalPages) {
            throw new InvalidPageNumberException(WebConstants.ERROR_INVALID_PAGE_NUMBER_MESSAGE);
        }
    }

    private String validateQuery(String query) {
        if (query != null && !query.isEmpty()) {
            return "%" + query.toLowerCase() + "%";
        }

        return query;
    }
}
