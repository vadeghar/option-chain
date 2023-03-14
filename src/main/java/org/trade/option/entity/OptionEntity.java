package org.trade.option.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("OptionEntity")
public class OptionEntity {
    @Id
    private UUID id;
    private String symbol;
    private LocalDateTime timestamp;
    private double underlyingValue;
    private List<Option> ceOptionList;
    private List<Option> peOptionList;
    private long totCeOI;
    private long totCeVol;
    private long totPeOI;
    private long totPeVol;
    private LocalDateTime lastUpdatedTs;
}
