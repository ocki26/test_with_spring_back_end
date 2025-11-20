package com.app_language.hoctiengtrung_online.Ticket.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookingRequestDTO {

    @NotBlank(message = "Show ID là bắt buộc")
    private String showId;

    @NotBlank(message = "Loại vé là bắt buộc")
    private String ticketTypeId;

    @Min(value = 1, message = "Số lượng vé phải lớn hơn 0")
    @Max(value = 10, message = "Tối đa 10 vé mỗi lần đặt")
    private Integer quantity;

    @NotBlank(message = "Tên người mua là bắt buộc")
    @Size(min = 2, max = 100, message = "Tên phải từ 2-100 ký tự")
    private String buyerName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email là bắt buộc")
    private String buyerEmail;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})", message = "Số điện thoại không hợp lệ")
    private String buyerPhone;

    private String soldBy;
}