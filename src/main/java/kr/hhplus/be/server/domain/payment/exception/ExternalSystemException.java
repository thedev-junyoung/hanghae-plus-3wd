package kr.hhplus.be.server.domain.payment.exception;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.common.exception.ErrorCode;

public class ExternalSystemException extends BusinessException {
    public ExternalSystemException(String reason) {
        super(ErrorCode.EXTERNAL_SYSTEM_ERROR, reason);
    }
}
