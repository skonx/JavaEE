/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.animals;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jsie
 */
@Embeddable
public class AdoptionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "client_id")
    private short clientId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "animal_id")
    private short animalId;

    public AdoptionPK() {
    }

    public AdoptionPK(short clientId, short animalId) {
        this.clientId = clientId;
        this.animalId = animalId;
    }

    public short getClientId() {
        return clientId;
    }

    public void setClientId(short clientId) {
        this.clientId = clientId;
    }

    public short getAnimalId() {
        return animalId;
    }

    public void setAnimalId(short animalId) {
        this.animalId = animalId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) clientId;
        hash += (int) animalId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdoptionPK)) {
            return false;
        }
        AdoptionPK other = (AdoptionPK) object;
        if (this.clientId != other.clientId) {
            return false;
        }
        if (this.animalId != other.animalId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.AdoptionPK[ clientId=" + clientId +
                ", animalId=" + animalId + " ]";
    }

}
