package com.app.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequestDto {
    private Long userId;
    private Long reservationId;
    private String information;
}
