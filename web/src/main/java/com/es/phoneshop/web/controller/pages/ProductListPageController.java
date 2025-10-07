package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.CartService;
import com.es.phoneshop.web.controller.Constants.WebConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Arrays;
import java.util.List;
import com.es.core.model.phone.PhoneDao;
import com.es.core.model.phone.Phone;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/productList")
public class ProductListPageController {
    @Resource
    private PhoneDao phoneDao;
    @Resource
    private CartService cartService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = WebConstants.BRAND_VALUE) String sortField,
            @RequestParam(required = false, defaultValue = WebConstants.ASC_VALUE) String sortOrder,
            Model model) {
        String validatedSortField = validateSortField(sortField);
        String validatedSortOrder = validateSortOrder(sortOrder);
        int offset = (page - 1) * WebConstants.PHONE_PAGE_AMOUNT;
        int totalPages = 0;
        List<Phone> phones;

        if (query != null && !query.isEmpty()) {
            String preparedQuery = "%" + query.toLowerCase() + "%";
            totalPages = (int) Math.ceil((double) phoneDao.getCountPhoneByQueryInStock(preparedQuery) / WebConstants.PHONE_PAGE_AMOUNT);
            phones = phoneDao.findAllByQueryInStock(preparedQuery, offset, WebConstants.PHONE_PAGE_AMOUNT, validatedSortField, validatedSortOrder);
        } else {
            totalPages = (int) Math.ceil((double) phoneDao.getPhoneInStockCount() / WebConstants.PHONE_PAGE_AMOUNT);
            phones = phoneDao.findAllInStockSorted(offset, WebConstants.PHONE_PAGE_AMOUNT, validatedSortField, validatedSortOrder);
        }

        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cartService.getCartQuantity());
        model.addAttribute(WebConstants.CART_COST_ATTR, cartService.getTotalCost());
        model.addAttribute(WebConstants.PHONES_ATTR, phones);
        model.addAttribute(WebConstants.CURR_PAGE_ATTR, page);
        model.addAttribute(WebConstants.TOTAL_PAGES_ATTR, totalPages);
        return "productList";
    }

    private String validateSortField(String sortField) {
        String validatedSortField = "p.%s";
        validatedSortField = String
                .format(validatedSortField,
                        Arrays.asList(WebConstants.BRAND_VALUE,
                                        WebConstants.MODEL_PARAM,
                                        WebConstants.PRICE_PARAM,
                                        WebConstants.DISPLAY_SIZE_PARAM)
                                .contains(sortField) ? sortField : WebConstants.ID_VALUE);

        return validatedSortField;
    }

    private String validateSortOrder(String sortOrder) {
        return Arrays.asList(WebConstants.ASC_VALUE, WebConstants.DESC_VALUE).contains(sortOrder) ? sortOrder : WebConstants.ASC_VALUE;
    }
}
