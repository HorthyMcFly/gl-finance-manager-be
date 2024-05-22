package com.gl.financemanager.income;

import com.gl.financemanager.auth.FmUser;
import com.gl.financemanager.period.FmPeriod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "INCOME")
public class Income {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FM_USER")
  private FmUser fmUser;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FM_PERIOD")
  private FmPeriod fmPeriod;

  private BigDecimal amount;

  private String source;

  private String comment;

  private boolean editable;

}
