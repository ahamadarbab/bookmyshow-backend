package com.bms.bookmyshow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterShowDto {
    private String ShowName;
    private LocalDateTime displayStartTime;
    private LocalDateTime displayEndTime;
}
