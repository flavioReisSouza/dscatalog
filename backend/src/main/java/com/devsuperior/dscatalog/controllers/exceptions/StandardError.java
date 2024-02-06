package com.devsuperior.dscatalog.controllers.exceptions;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class StandardError {

  private Instant timestamp;
  private Integer status;
  private String error;
  private String message;
  private String path;
}
