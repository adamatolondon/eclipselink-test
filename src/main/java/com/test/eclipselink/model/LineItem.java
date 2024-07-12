/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.eclipselink.model;

import javax.persistence.*;

/**
 *
 * @author Antonio Damato <anto.damato@gmail.com>
 */
@Entity
@Table(name = "line_item")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean shipped;

    @ManyToOne
    private SimpleProduct product;

    public Long getId() {
	return id;
    }

    public Boolean getShipped() {
	return shipped;
    }

    public void setShipped(Boolean shipped) {
	this.shipped = shipped;
    }

    public SimpleProduct getProduct() {
	return product;
    }

    public void setProduct(SimpleProduct product) {
	this.product = product;
    }

}
