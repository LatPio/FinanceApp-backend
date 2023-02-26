package pl.finansepal.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Income {

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
    @Column(name = "Date")
    private LocalDateTime date;
    @ManyToMany()
//    @JoinColumn(name = "tags_id", referencedColumnName = "ID")
    @JoinTable(
            name = "Tags_Income",
            joinColumns = @JoinColumn(name = "income_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
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
