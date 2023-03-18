package pl.finansepal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "User_Options")
public class UserOptions {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    private String userPagination;
    private String defaultCurrency;
    private Integer getLastNumberOfMonthsData;
//    @OneToOne(mappedBy = "userOptions")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
