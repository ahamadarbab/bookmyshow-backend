package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterShowPriceMappingDto {

    private UUID showId;
    private UUID hallId;
    private UUID hallRowMappingId;
    private Double price;
}
