package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString

public class ItemDto {

    private String id;
    private String desc;
    private double unitPrice;
    private int qty;
}
