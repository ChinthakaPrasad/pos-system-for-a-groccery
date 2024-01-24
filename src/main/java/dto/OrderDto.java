package dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class OrderDto {

    private String orderId;
    private String date;
    private String customerID;
    private List<OrderDetailsDto> list;
}
