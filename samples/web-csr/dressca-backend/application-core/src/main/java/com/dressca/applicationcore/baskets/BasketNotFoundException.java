package com.dressca.applicationcore.baskets;

import com.dressca.systemcommon.constant.ExceptionIdConstant;
import com.dressca.systemcommon.exception.LogicException;

public class BasketNotFoundException extends LogicException {
    public BasketNotFoundException(long basketId) {
        super(null, ExceptionIdConstant.E_BASKET0001, new String[] {String.valueOf(basketId)}, new String[] {String.valueOf(basketId)});
    }
}
