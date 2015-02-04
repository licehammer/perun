/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.metacentrum.perun.core.api;

/**
 *
 * @author Katarína Hrabovská <katarina.hrabovska1992@gmail.com>
 */
public class AttributeHolders {
	protected PerunBean primary;
	protected PerunBean secondary;

	public AttributeHolders(PerunBean primary, PerunBean secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

	public PerunBean getPrimary() {
		return primary;
	}

	public void setPrimary(PerunBean primary) {
		this.primary = primary;
	}

	public PerunBean getSecondary() {
		return secondary;
	}

	public void setSecondary(PerunBean secondary) {
		this.secondary = secondary;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + getPrimary().getId();
		if (getSecondary()!=null) {
			hash = 53 * hash + getSecondary().getId();
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AttributeHolders)) {
			return false;
		}
		AttributeHolders attrHolders = (AttributeHolders) obj;
		if (getSecondary()==null) {
			return this.getPrimary().equals(attrHolders.getPrimary());
		}
		boolean rightEquals = ((this.getPrimary().equals(attrHolders.getPrimary())) &&
				(this.getSecondary().equals(attrHolders.getSecondary())));
		boolean reverseEquals = ((this.getPrimary().equals(attrHolders.getSecondary())) &&
				(this.getSecondary().equals(attrHolders.getPrimary())));
		return (rightEquals || reverseEquals);
	}

	@Override
	public String toString() {
		if (getSecondary()==null) {
			return getPrimary().toString();
		}
		return "[" + getPrimary().toString() + ", " + getSecondary().toString() + "]";
	}

}
