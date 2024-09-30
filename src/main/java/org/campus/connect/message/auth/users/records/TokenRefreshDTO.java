package org.campus.connect.message.auth.users.records;

public record TokenRefreshDTO(String email, String refreshToken) {
}
