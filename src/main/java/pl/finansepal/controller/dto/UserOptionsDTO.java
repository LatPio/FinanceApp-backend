package pl.finansepal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOptionsDTO {

    private Long id;
    private String userPagination;
    private String defaultCurrency;
    private Integer getLastNumberOfMonthsData;
    private UserDTO user;

}
