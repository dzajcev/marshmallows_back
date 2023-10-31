package com.dzaitsev.marshmallows.dto;

import com.dzaitsev.marshmallows.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDto {
    private ErrorCode code;

    private String message;
}
