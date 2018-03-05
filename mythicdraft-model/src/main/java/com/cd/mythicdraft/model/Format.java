package com.cd.mythicdraft.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FORMAT")
public class Format implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "FIRST_PACK",
			insertable = false,
			updatable = false)	
	private Integer firstPack;

	@ManyToOne(optional = false)
	@JoinColumn(name = "FIRST_PACK")
	private Set firstPackSet;	
	
	@Column(name = "SECOND_PACK",
			insertable = false,
			updatable = false)
	private Integer secondPack;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "SECOND_PACK")
	private Set secondPackSet;	
	
	@Column(name = "THIRD_PACK",
			insertable = false,
			updatable = false)
	private Integer thirdPack;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "THIRD_PACK")
	private Set thirdPackSet;

	public Format() {}
	
	public Format(Integer firstPack, Integer secondPack, Integer thirdPack) {
		this.firstPack = firstPack;
		this.secondPack = secondPack;
		this.thirdPack = thirdPack;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFirstPack() {
		return firstPack;
	}

	public void setFirstPack(Integer firstPack) {
		this.firstPack = firstPack;
	}

	public Set getFirstPackSet() {
		return firstPackSet;
	}

	public void setFirstPackSet(Set firstPackSet) {
		this.firstPackSet = firstPackSet;
	}

	public Integer getSecondPack() {
		return secondPack;
	}

	public void setSecondPack(Integer secondPack) {
		this.secondPack = secondPack;
	}

	public Set getSecondPackSet() {
		return secondPackSet;
	}

	public void setSecondPackSet(Set secondPackSet) {
		this.secondPackSet = secondPackSet;
	}

	public Integer getThirdPack() {
		return thirdPack;
	}

	public void setThirdPack(Integer thirdPack) {
		this.thirdPack = thirdPack;
	}

	public Set getThirdPackSet() {
		return thirdPackSet;
	}

	public void setThirdPackSet(Set thirdPackSet) {
		this.thirdPackSet = thirdPackSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstPack == null) ? 0 : firstPack.hashCode());
		result = prime * result
				+ ((secondPack == null) ? 0 : secondPack.hashCode());
		result = prime * result
				+ ((thirdPack == null) ? 0 : thirdPack.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Format other = (Format) obj;
		if (firstPack == null) {
			if (other.firstPack != null)
				return false;
		} else if (!firstPack.equals(other.firstPack))
			return false;
		if (secondPack == null) {
			if (other.secondPack != null)
				return false;
		} else if (!secondPack.equals(other.secondPack))
			return false;
		if (thirdPack == null) {
			if (other.thirdPack != null)
				return false;
		} else if (!thirdPack.equals(other.thirdPack))
			return false;
		return true;
	}	
}
