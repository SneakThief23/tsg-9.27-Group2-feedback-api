package com.tsg.feedbackapi.dtos;

import java.time.OffsetDateTime;

public record ErrorResponseDTO(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path
)
{}
