package com.example.shopapp.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
        * MethodArgumentNotValidException
        Công dụng: Xử lý các trường hợp mà các tham số của phương thức không hợp lệ,
            thường xuất hiện khi các tham số không đáp ứng các ràng buộc validation.
        Ví dụ: Nếu một trường trong request body có giá trị null nhưng được đánh dấu là @NotNull, exception này sẽ được ném ra.

        * MissingServletRequestParameterException
        Công dụng: Xử lý các trường hợp thiếu các tham số yêu cầu trong request.
        Ví dụ: Nếu một request yêu cầu tham số id nhưng request không có tham số id, exception này sẽ được ném ra.

        *ConstraintViolationException
        Công dụng: Xử lý các trường hợp vi phạm ràng buộc dữ liệu,
             thường xuất hiện khi các tham số phương thức không đáp ứng các ràng buộc validation
             được định nghĩa bằng các annotation như @Size, @Min, @Max, etc.
        Ví dụ: Nếu một tham số có giá trị lớn hơn giá trị tối đa được phép, exception này sẽ được ném ra.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));

        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            int start = message.lastIndexOf("[") + 1;
            int end = message.lastIndexOf("]") - 1;
            message = message.substring(start, end);
            errorResponse.setError("Invalid Payload");
            errorResponse.setMessage(message);
        } else if (e instanceof MissingServletRequestParameterException) {
            errorResponse.setError("Invalid Parameter");
            errorResponse.setMessage(message);
        } else if (e instanceof ConstraintViolationException) {
            errorResponse.setError("Invalid Parameter");
            errorResponse.setMessage(message.substring(message.indexOf(" ") + 1));
        } else {
            errorResponse.setError("Invalid Data");
            errorResponse.setMessage(message);
        }
        return errorResponse;
    }
    /**
     * Handle exception when the request not found data
     *
     * @param e
     * @param request
     * @return
     */
    /*
    * ResourceNotFoundException
    Công dụng: Xử lý các trường hợp không tìm thấy tài nguyên được yêu cầu.
    Ví dụ: Nếu một yêu cầu tìm kiếm người dùng với ID cụ thể nhưng người dùng này không tồn tại, exception này sẽ được ném ra.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    /**
     * Handle exception when the data is conflicted
     *
     * @param e
     * @param request
     * @return
     */
    /*
    *InvalidDataException
    Công dụng: Xử lý các trường hợp dữ liệu không hợp lệ gây ra xung đột.
    Ví dụ: Nếu dữ liệu thêm vào hoặc cập nhật đã tồn tại hoặc gây xung đột, exception này sẽ được ném ra.
     */
    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateKeyException(InvalidDataException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError(HttpStatus.CONFLICT.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }

    /**
     * Handle exception when the request method is not supported
     *
     * @param e
     * @param request
     * @return
     */
    /*
    * HttpRequestMethodNotSupportedException
    Công dụng: Xử lý các yêu cầu với phương thức HTTP không được hỗ trợ.
    Ví dụ: Nếu một endpoint chỉ hỗ trợ GET nhưng client gửi yêu cầu POST, exception này sẽ được ném ra.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        errorResponse.setError(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
    /**
     * Handle exception when the media type is not supported
     *
     * @param e
     * @param request
     * @return
     */
    /*
    * HttpMediaTypeNotSupportedException
    Công dụng: Xử lý các yêu cầu với kiểu phương tiện (MIME type) không được hỗ trợ.
    Ví dụ: Nếu server chỉ hỗ trợ application/json nhưng client gửi yêu cầu với application/xml, exception này sẽ được ném ra.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        errorResponse.setError(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
    /**
     * Handle exception when access is denied
     *
     * @param e
     * @param request
     * @return
     */
    /*
    * AccessDeniedException
    Công dụng: Xử lý các trường hợp truy cập bị từ chối do quyền hạn.
    Ví dụ: Nếu người dùng không có quyền truy cập vào tài nguyên cụ thể, exception này sẽ được ném ra.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
        errorResponse.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    /**
     * Handle exception when data integrity is violated
     *
     * @param e
     * @param request
     * @return
     */
    /*
    * DataIntegrityViolationException
    Công dụng: Xử lý các trường hợp vi phạm ràng buộc dữ liệu trong database.
    Ví dụ: Nếu một ràng buộc unique bị vi phạm, exception này sẽ được ném ra.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError(HttpStatus.CONFLICT.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
    /**
     * Handle exception when message not readable
     *
     * @param e
     * @param request
     * @return
     */
    /*
    * HttpMessageNotReadableException
    Công dụng: Xử lý các trường hợp khi payload không thể đọc được (ví dụ: JSON không hợp lệ).
    Ví dụ: Nếu payload của yêu cầu không thể phân tích được thành đối tượng tương ứng, exception này sẽ được ném ra.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage("Malformed JSON request");
        return errorResponse;
    }

    /**
     * Handle exception when internal server error
     *
     * @param e
     * @param request
     * @return error
     */
    /*
    * Exception
    Công dụng: Xử lý tất cả các exception khác không được xử lý riêng biệt.
    Ví dụ: Bất kỳ exception nào không thuộc các loại trên đều sẽ được xử lý bởi handler này.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setMessage(e.getMessage());

        return errorResponse;
    }
}
