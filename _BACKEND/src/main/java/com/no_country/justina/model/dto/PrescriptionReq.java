package com.no_country.justina.model.dto;

import com.no_country.justina.model.entities.Treatment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionReq {
  @NotNull private long treatmentId;
}
