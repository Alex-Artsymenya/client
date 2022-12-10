package com.example.bsuir.models.TCP;

import com.example.bsuir.enums.Responses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Responses responseStatus;
    private String responseMessage;
    private String responseData;
}
