/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.animals;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
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
@Table(name = "Animal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Animal.findAll", query = "SELECT a FROM Animal a")
    ,
        @NamedQuery(name = "Animal.findById",
            query = "SELECT a FROM Animal a WHERE a.id = :id")
    ,
        @NamedQuery(name = "Animal.findBySexe",
            query = "SELECT a FROM Animal a WHERE a.sexe = :sexe")
    ,
        @NamedQuery(name = "Animal.findByDateNaissance",
            query = "SELECT a FROM Animal a WHERE a.dateNaissance = :dateNaissance")
    ,
        @NamedQuery(name = "Animal.findByNom",
            query = "SELECT a FROM Animal a WHERE a.nom = :nom")
    ,
        @NamedQuery(name = "Animal.findByDisponible",
            query = "SELECT a FROM Animal a WHERE a.disponible = :disponible")})
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Column(name = "sexe")
    private Character sexe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_naissance")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateNaissance;
    @Size(max = 30)
    @Column(name = "nom")
    private String nom;
    @Lob
    @Size(max = 65535)
    @Column(name = "commentaires")
    private String commentaires;
    @Column(name = "disponible")
    private Boolean disponible;
    @JoinColumn(name = "espece_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Espece especeId;
    @OneToMany(mappedBy = "mereId")
    private Collection<Animal> animalCollection;
    @JoinColumn(name = "mere_id", referencedColumnName = "id")
    @ManyToOne
    private Animal mereId;
    @OneToMany(mappedBy = "pereId")
    private Collection<Animal> animalCollection1;
    @JoinColumn(name = "pere_id", referencedColumnName = "id")
    @ManyToOne
    private Animal pereId;
    @JoinColumn(name = "race_id", referencedColumnName = "id")
    @ManyToOne
    private Race raceId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "animal")
    private Adoption adoption;

    public Animal() {
    }

    public Animal(Short id) {
        this.id = id;
    }

    public Animal(Short id, Date dateNaissance) {
        this.id = id;
        this.dateNaissance = dateNaissance;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Character getSexe() {
        return sexe;
    }

    public void setSexe(Character sexe) {
        this.sexe = sexe;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Espece getEspeceId() {
        return especeId;
    }

    public void setEspeceId(Espece especeId) {
        this.especeId = especeId;
    }

    @XmlTransient
    public Collection<Animal> getAnimalCollection() {
        return animalCollection;
    }

    public void setAnimalCollection(Collection<Animal> animalCollection) {
        this.animalCollection = animalCollection;
    }

    public Animal getMereId() {
        return mereId;
    }

    public void setMereId(Animal mereId) {
        this.mereId = mereId;
    }

    @XmlTransient
    public Collection<Animal> getAnimalCollection1() {
        return animalCollection1;
    }

    public void setAnimalCollection1(Collection<Animal> animalCollection1) {
        this.animalCollection1 = animalCollection1;
    }

    public Animal getPereId() {
        return pereId;
    }

    public void setPereId(Animal pereId) {
        this.pereId = pereId;
    }

    public Race getRaceId() {
        return raceId;
    }

    public void setRaceId(Race raceId) {
        this.raceId = raceId;
    }

    public Adoption getAdoption() {
        return adoption;
    }

    public void setAdoption(Adoption adoption) {
        this.adoption = adoption;
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
        if (!(object instanceof Animal)) {
            return false;
        }
        Animal other = (Animal) object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.Animal[ id=" + id + " ]";
    }

}
