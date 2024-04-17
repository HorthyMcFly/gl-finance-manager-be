package com.gl.financemanager.period;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PERIOD")
public class Period {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
}
