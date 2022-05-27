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
public class OptionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String symbol;
    private LocalDate curDate;
    private LocalDateTime updatedAt;
    @CreationTimestamp
    private LocalDateTime addedAt;
    private String expiry;
    private Double spotPrice;
    private Integer strikePrice;
    private Long oi;
    private Long changeInOi;
    private Long netChangeInOi;
    private String optionType;
    private String sourceSystem;
}
