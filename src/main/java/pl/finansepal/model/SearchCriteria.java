package pl.finansepal.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchCriteria {

    private String key;
    private Object value;
    private SearchOperation operation;

}
