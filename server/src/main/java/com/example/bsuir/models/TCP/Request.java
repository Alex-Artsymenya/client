package com.example.bsuir.models.TCP;

import com.example.bsuir.enums.Requests;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Requests requestType;
    private String requestMessage;
}
