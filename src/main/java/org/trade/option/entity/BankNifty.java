package org.trade.option.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankNifty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Integer strikePrice;
    private String optionType;
    private String expiry;
    private String source;
    private Double lastPrice;
    private Long totalOi;
    private Long changeInOi;
    private Long curChangeInOi;
    private String updatedAtSource;
    @CreationTimestamp
    private LocalDateTime addedTs;
}
