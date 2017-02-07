/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.animals;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jsie
 */
@Embeddable
public class AnimalhistoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private short id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_histo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateHisto;

    public AnimalhistoPK() {
    }

    public AnimalhistoPK(short id, Date dateHisto) {
        this.id = id;
        this.dateHisto = dateHisto;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public Date getDateHisto() {
        return dateHisto;
    }

    public void setDateHisto(Date dateHisto) {
        this.dateHisto = dateHisto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (dateHisto != null ? dateHisto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnimalhistoPK)) {
            return false;
        }
        AnimalhistoPK other = (AnimalhistoPK) object;
        if (this.id != other.id) {
            return false;
        }
        if ((this.dateHisto == null && other.dateHisto != null) ||
                (this.dateHisto != null &&
                !this.dateHisto.equals(other.dateHisto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.AnimalhistoPK[ id=" + id + ", dateHisto=" +
                dateHisto + " ]";
    }

}
