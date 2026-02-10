package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterHallRowMappingDto {
    private UUID hallId;
    private String rowType;
    private String rowRange;
    private int seatCount;
}
