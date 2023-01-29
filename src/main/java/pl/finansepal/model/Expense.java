package pl.finansepal.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {


    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    @Column(name = "Amount")
    private BigDecimal amount;
    @Column(name = "Currency")
    private String currency;
    @Column(name = "Name")
    private String name;
    @Column (name = "Tags")
    @OneToMany(mappedBy = "id")
    private List<Tag> tags;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @CreationTimestamp
    @Column(name = "CreationDate")
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "UpdateDate")
    private Instant updateAt;
}
