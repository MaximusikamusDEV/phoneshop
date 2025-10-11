package com.es.phoneshop.web.controller;

import com.es.phoneshop.web.dto.CartItemDto;
import com.es.core.cart.exceptions.CartValidationException;
import com.es.core.cart.exceptions.ItemNotExistException;
import com.es.phoneshop.web.dto.AjaxCartResponseDto;
import com.es.phoneshop.web.services.CartDisplayService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartDisplayService cartDisplayService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AjaxCartResponseDto> addPhone(@Valid @RequestBody CartItemDto cartItemDto,
                                                        BindingResult bindingResult) throws ItemNotExistException {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            throw new CartValidationException(errorMsg);
        }

        AjaxCartResponseDto response = cartDisplayService.addPhone(cartItemDto);

        return ResponseEntity.ok().body(response);
    }
}
