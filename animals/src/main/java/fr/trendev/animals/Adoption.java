/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.animals;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsie
 */
@Entity
@Table(name = "Adoption")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Adoption.findAll", query = "SELECT a FROM Adoption a")
    ,
        @NamedQuery(name = "Adoption.findByClientId",
            query = "SELECT a FROM Adoption a WHERE a.adoptionPK.clientId = :clientId")
    ,
        @NamedQuery(name = "Adoption.findByAnimalId",
            query = "SELECT a FROM Adoption a WHERE a.adoptionPK.animalId = :animalId")
    ,
        @NamedQuery(name = "Adoption.findByDateReservation",
            query = "SELECT a FROM Adoption a WHERE a.dateReservation = :dateReservation")
    ,
        @NamedQuery(name = "Adoption.findByDateAdoption",
            query = "SELECT a FROM Adoption a WHERE a.dateAdoption = :dateAdoption")
    ,
        @NamedQuery(name = "Adoption.findByPrix",
            query = "SELECT a FROM Adoption a WHERE a.prix = :prix")
    ,
        @NamedQuery(name = "Adoption.findByPaye",
            query = "SELECT a FROM Adoption a WHERE a.paye = :paye")})
public class Adoption implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AdoptionPK adoptionPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_reservation")
    @Temporal(TemporalType.DATE)
    private Date dateReservation;
    @Column(name = "date_adoption")
    @Temporal(TemporalType.DATE)
    private Date dateAdoption;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "prix")
    private BigDecimal prix;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paye")
    private boolean paye;
    @JoinColumn(name = "animal_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Animal animal;
    @JoinColumn(name = "client_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Client client;

    public Adoption() {
    }

    public Adoption(AdoptionPK adoptionPK) {
        this.adoptionPK = adoptionPK;
    }

    public Adoption(AdoptionPK adoptionPK, Date dateReservation, BigDecimal prix,
            boolean paye) {
        this.adoptionPK = adoptionPK;
        this.dateReservation = dateReservation;
        this.prix = prix;
        this.paye = paye;
    }

    public Adoption(short clientId, short animalId) {
        this.adoptionPK = new AdoptionPK(clientId, animalId);
    }

    public AdoptionPK getAdoptionPK() {
        return adoptionPK;
    }

    public void setAdoptionPK(AdoptionPK adoptionPK) {
        this.adoptionPK = adoptionPK;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Date getDateAdoption() {
        return dateAdoption;
    }

    public void setDateAdoption(Date dateAdoption) {
        this.dateAdoption = dateAdoption;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public boolean getPaye() {
        return paye;
    }

    public void setPaye(boolean paye) {
        this.paye = paye;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (adoptionPK != null ? adoptionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Adoption)) {
            return false;
        }
        Adoption other = (Adoption) object;
        if ((this.adoptionPK == null && other.adoptionPK != null) ||
                (this.adoptionPK != null &&
                !this.adoptionPK.equals(other.adoptionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.Adoption[ adoptionPK=" + adoptionPK + " ]";
    }

}
