package com.daypaytechnologies.user.domain;

import com.daypaytechnologies.core.domain.AbstractPersistableCustom;
import com.daypaytechnologies.portfolio.client.domain.Client;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "m_selfservice_user_client_mapping")
public class AppUserClientMapping extends AbstractPersistableCustom<Long> {
	
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

	public AppUserClientMapping(){
		
	}

	public AppUserClientMapping(Client client){
		this.client = client;
	}

	public Client getClient() {
		return this.client;
	}
	
	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		AppUserClientMapping that = (AppUserClientMapping) obj;

		return null == this.client.getId() ? false : this.client.getId().equals(that.client.getId());
	}
	
	@Override
	public int hashCode() {

		int hashCode = 17;

		hashCode += null == this.client ? 0 : this.client.getId().hashCode() * 31;

		return hashCode;
	}

}
