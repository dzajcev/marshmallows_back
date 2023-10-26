package com.dzaitsev.marshmallows.dto;

import com.dzaitsev.marshmallows.exceptions.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorDto {
    private ErrorCodes code;
}
