package zerobase.stockdividends.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Company {
    private String ticker;
    private String name;
}
