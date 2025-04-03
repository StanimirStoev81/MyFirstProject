package org.example.myfirstproject.Models.DTO;

import java.time.LocalDateTime;

public class ReviewDTO {


        private Long id;
        private String username;
        private String message;
        private int rating;
        private LocalDateTime timestamp;

        // Конструктори
        public ReviewDTO() {}

        public ReviewDTO(Long id, String username, String message, int rating, LocalDateTime timestamp) {
            this.id = id;
            this.username = username;
            this.message = message;
            this.rating = rating;
            this.timestamp = timestamp;
        }

        // Гетъри и Сетъри
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }


