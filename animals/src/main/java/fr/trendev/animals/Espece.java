/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.animals;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jsie
 */
@Entity
@Table(name = "Espece")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Espece.findAll", query = "SELECT e FROM Espece e")
    ,
        @NamedQuery(name = "Espece.findById",
            query = "SELECT e FROM Espece e WHERE e.id = :id")
    ,
        @NamedQuery(name = "Espece.findByNomCourant",
            query = "SELECT e FROM Espece e WHERE e.nomCourant = :nomCourant")
    ,
        @NamedQuery(name = "Espece.findByNomLatin",
            query = "SELECT e FROM Espece e WHERE e.nomLatin = :nomLatin")
    ,
        @NamedQuery(name = "Espece.findByPrix",
            query = "SELECT e FROM Espece e WHERE e.prix = :prix")})
public class Espece implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nom_courant")
    private String nomCourant;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nom_latin")
    private String nomLatin;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private BigDecimal prix;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "especeId")
    private Collection<Animal> animalCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "especeId")
    private Collection<Race> raceCollection;

    public Espece() {
    }

    public Espece(Short id) {
        this.id = id;
    }

    public Espece(Short id, String nomCourant, String nomLatin) {
        this.id = id;
        this.nomCourant = nomCourant;
        this.nomLatin = nomLatin;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNomCourant() {
        return nomCourant;
    }

    public void setNomCourant(String nomCourant) {
        this.nomCourant = nomCourant;
    }

    public String getNomLatin() {
        return nomLatin;
    }

    public void setNomLatin(String nomLatin) {
        this.nomLatin = nomLatin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    @XmlTransient
    public Collection<Animal> getAnimalCollection() {
        return animalCollection;
    }

    public void setAnimalCollection(Collection<Animal> animalCollection) {
        this.animalCollection = animalCollection;
    }

    @XmlTransient
    public Collection<Race> getRaceCollection() {
        return raceCollection;
    }

    public void setRaceCollection(Collection<Race> raceCollection) {
        this.raceCollection = raceCollection;
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
        if (!(object instanceof Espece)) {
            return false;
        }
        Espece other = (Espece) object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.Espece[ id=" + id + " ]";
    }

}
