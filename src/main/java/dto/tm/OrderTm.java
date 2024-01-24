package dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class OrderTm {
    private String code;
    private String desc;
    private int qty;
    private double amount;
    private Button btn;
}
