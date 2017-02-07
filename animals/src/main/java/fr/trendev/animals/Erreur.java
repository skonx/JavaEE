/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.animals;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsie
 */
@Entity
@Table(name = "Erreur")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Erreur.findAll", query = "SELECT e FROM Erreur e")
    ,
        @NamedQuery(name = "Erreur.findById",
            query = "SELECT e FROM Erreur e WHERE e.id = :id")
    ,
        @NamedQuery(name = "Erreur.findByErreur",
            query = "SELECT e FROM Erreur e WHERE e.erreur = :erreur")})
public class Erreur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Size(max = 255)
    @Column(name = "erreur")
    private String erreur;

    public Erreur() {
    }

    public Erreur(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Erreur)) {
            return false;
        }
        Erreur other = (Erreur) object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.Erreur[ id=" + id + " ]";
    }

}
