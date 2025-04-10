package kr.hhplus.be.server.domain.product.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class DescriptionTooLongException extends BusinessException {
    public DescriptionTooLongException(int maxLength) {
        super(ErrorCode.INVALID_INPUT_VALUE, "설명은 " + maxLength + "자 이하만 가능합니다.");
    }
}