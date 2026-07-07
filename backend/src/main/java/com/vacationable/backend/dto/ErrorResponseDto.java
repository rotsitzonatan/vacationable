package com.vacationable.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorResponseDto {

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public ErrorResponseDto(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

}
