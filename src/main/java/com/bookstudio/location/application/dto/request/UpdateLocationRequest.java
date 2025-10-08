package com.bookstudio.location.application.dto.request;

import java.util.List;

public record UpdateLocationRequest(
    String name,
    String description,
    List<UpdateShelfRequest> shelves
) {}
