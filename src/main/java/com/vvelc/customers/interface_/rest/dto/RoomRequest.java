package com.vvelc.customers.interface_.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoomRequest(
        @NotBlank(message = "Room number is required")
        @Size(min = 1, max = 10, message = "Room number must be between 1 and 10 characters")
        String number
) {}
