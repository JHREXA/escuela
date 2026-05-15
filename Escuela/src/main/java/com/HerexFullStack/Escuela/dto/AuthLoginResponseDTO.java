package com.HerexFullStack.Escuela.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message","jwt", "status"})
public record AuthLoginResponseDTO (String username,
                                    String message,
                                    String jwt,
                                    boolean status
                                    ){
}
