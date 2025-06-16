package com.picpaysimplificado.DTO;

import java.math.BigDecimal;

public record TransacationDTO (BigDecimal value, Long senderId, Long receiverId) {

}
