package com.es.phoneshop.web.controller.pages;

import com.es.core.model.phone.Phone;
import com.es.phoneshop.web.constants.WebConstants;
import com.es.phoneshop.web.dto.ProductListPageDto;
import com.es.phoneshop.web.services.CartDisplayService;
import com.es.phoneshop.web.services.PhoneDisplayService;
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
    private CartDisplayService cartDisplayService;
    @Resource
    PhoneDisplayService phoneDisplayService;

    @RequestMapping(method = RequestMethod.GET)
    public String showProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = WebConstants.BRAND_VALUE) String sortField,
            @RequestParam(required = false, defaultValue = WebConstants.ASC_VALUE) String sortOrder,
            Model model) {
        ProductListPageDto productListPageDto = phoneDisplayService.getProductListPageDto(page, query, sortField, sortOrder);
        List<Phone> phones = productListPageDto.getPhones();
        int totalPages = productListPageDto.getTotalPages();

        model.addAttribute(WebConstants.CART_QUANTITY_ATTR, cartDisplayService.getTotalQuantity());
        model.addAttribute(WebConstants.CART_COST_ATTR, cartDisplayService.getTotalCost());
        model.addAttribute(WebConstants.PHONES_ATTR, phones);
        model.addAttribute(WebConstants.CURR_PAGE_ATTR, page);
        model.addAttribute(WebConstants.TOTAL_PAGES_ATTR, totalPages);
        return "productList";
    }
}
