package pl.finansepal.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {


    @TableGenerator(name = "id_generator", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_value",
            pkColumnValue="task_gen", initialValue=10000, allocationSize=10)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_generator")
    @Column(name = "ID")
    private Long id;
    @Column(name = "Amount")
    private BigDecimal amount;
    @Column(name = "Currency")
    private String currency;
    @Column(name = "Name")
    private String name;
    @Column(name = "Date")
    private LocalDateTime date;
    @ManyToMany()
    @JoinTable(
            name = "Tags_Expense",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "ID")
    private User user;
    @CreationTimestamp
    @Column(name = "CreationDate")
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "UpdateDate")
    private Instant updateAt;
}
