/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.animals;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jsie
 */
@Entity
@Table(name = "Race")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Race.findAll", query = "SELECT r FROM Race r")
    ,
        @NamedQuery(name = "Race.findById",
            query = "SELECT r FROM Race r WHERE r.id = :id")
    ,
        @NamedQuery(name = "Race.findByNom",
            query = "SELECT r FROM Race r WHERE r.nom = :nom")
    ,
        @NamedQuery(name = "Race.findByPrix",
            query = "SELECT r FROM Race r WHERE r.prix = :prix")
    ,
        @NamedQuery(name = "Race.findByDateInsertion",
            query = "SELECT r FROM Race r WHERE r.dateInsertion = :dateInsertion")
    ,
        @NamedQuery(name = "Race.findByUtilisateurInsertion",
            query = "SELECT r FROM Race r WHERE r.utilisateurInsertion = :utilisateurInsertion")
    ,
        @NamedQuery(name = "Race.findByDateModification",
            query = "SELECT r FROM Race r WHERE r.dateModification = :dateModification")
    ,
        @NamedQuery(name = "Race.findByUtilisateurModification",
            query = "SELECT r FROM Race r WHERE r.utilisateurModification = :utilisateurModification")})
public class Race implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nom")
    private String nom;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private BigDecimal prix;
    @Column(name = "date_insertion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInsertion;
    @Size(max = 20)
    @Column(name = "utilisateur_insertion")
    private String utilisateurInsertion;
    @Column(name = "date_modification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateModification;
    @Size(max = 20)
    @Column(name = "utilisateur_modification")
    private String utilisateurModification;
    @OneToMany(mappedBy = "raceId")
    private Collection<Animal> animalCollection;
    @JoinColumn(name = "espece_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Espece especeId;

    public Race() {
    }

    public Race(Short id) {
        this.id = id;
    }

    public Race(Short id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public Date getDateInsertion() {
        return dateInsertion;
    }

    public void setDateInsertion(Date dateInsertion) {
        this.dateInsertion = dateInsertion;
    }

    public String getUtilisateurInsertion() {
        return utilisateurInsertion;
    }

    public void setUtilisateurInsertion(String utilisateurInsertion) {
        this.utilisateurInsertion = utilisateurInsertion;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }

    public String getUtilisateurModification() {
        return utilisateurModification;
    }

    public void setUtilisateurModification(String utilisateurModification) {
        this.utilisateurModification = utilisateurModification;
    }

    @XmlTransient
    public Collection<Animal> getAnimalCollection() {
        return animalCollection;
    }

    public void setAnimalCollection(Collection<Animal> animalCollection) {
        this.animalCollection = animalCollection;
    }

    public Espece getEspeceId() {
        return especeId;
    }

    public void setEspeceId(Espece especeId) {
        this.especeId = especeId;
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
        if (!(object instanceof Race)) {
            return false;
        }
        Race other = (Race) object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.Race[ id=" + id + " ]";
    }

}
