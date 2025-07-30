package com.bookstudio.worker.projection;

public interface WorkerViewProjection {
    Long getWorkerId();
    String getUsername();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getRole();
    String getProfilePhotoUrl();
    String getStatus();
}
