package com.ferreteria.ferreteria.entities.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class LowLogic extends Base{
    private boolean inactive;
}
