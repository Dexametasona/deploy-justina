package com.no_country.justina.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRes {
  private long id;
  private String name;
  private String lastname;
  private String email;
  private String password;
  private LocalDateTime createdAt;
  private boolean isEnable;
}
