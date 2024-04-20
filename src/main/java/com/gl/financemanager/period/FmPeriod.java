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
@Table(name = "FM_PERIOD")
public class FmPeriod {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
}
