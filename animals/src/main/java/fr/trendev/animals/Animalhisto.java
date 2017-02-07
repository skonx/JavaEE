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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsie
 */
@Entity
@Table(name = "Animal_histo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Animalhisto.findAll",
            query = "SELECT a FROM Animalhisto a")
    ,
        @NamedQuery(name = "Animalhisto.findById",
            query = "SELECT a FROM Animalhisto a WHERE a.animalhistoPK.id = :id")
    ,
        @NamedQuery(name = "Animalhisto.findBySexe",
            query = "SELECT a FROM Animalhisto a WHERE a.sexe = :sexe")
    ,
        @NamedQuery(name = "Animalhisto.findByDateNaissance",
            query = "SELECT a FROM Animalhisto a WHERE a.dateNaissance = :dateNaissance")
    ,
        @NamedQuery(name = "Animalhisto.findByNom",
            query = "SELECT a FROM Animalhisto a WHERE a.nom = :nom")
    ,
        @NamedQuery(name = "Animalhisto.findByEspeceId",
            query = "SELECT a FROM Animalhisto a WHERE a.especeId = :especeId")
    ,
        @NamedQuery(name = "Animalhisto.findByRaceId",
            query = "SELECT a FROM Animalhisto a WHERE a.raceId = :raceId")
    ,
        @NamedQuery(name = "Animalhisto.findByMereId",
            query = "SELECT a FROM Animalhisto a WHERE a.mereId = :mereId")
    ,
        @NamedQuery(name = "Animalhisto.findByPereId",
            query = "SELECT a FROM Animalhisto a WHERE a.pereId = :pereId")
    ,
        @NamedQuery(name = "Animalhisto.findByDisponible",
            query = "SELECT a FROM Animalhisto a WHERE a.disponible = :disponible")
    ,
        @NamedQuery(name = "Animalhisto.findByDateHisto",
            query = "SELECT a FROM Animalhisto a WHERE a.animalhistoPK.dateHisto = :dateHisto")
    ,
        @NamedQuery(name = "Animalhisto.findByUtilisateurHisto",
            query = "SELECT a FROM Animalhisto a WHERE a.utilisateurHisto = :utilisateurHisto")
    ,
        @NamedQuery(name = "Animalhisto.findByEvenementHisto",
            query = "SELECT a FROM Animalhisto a WHERE a.evenementHisto = :evenementHisto")})
public class Animalhisto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AnimalhistoPK animalhistoPK;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "espece_id")
    private short especeId;
    @Column(name = "race_id")
    private Short raceId;
    @Column(name = "mere_id")
    private Short mereId;
    @Column(name = "pere_id")
    private Short pereId;
    @Column(name = "disponible")
    private Boolean disponible;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "utilisateur_histo")
    private String utilisateurHisto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "evenement_histo")
    private String evenementHisto;

    public Animalhisto() {
    }

    public Animalhisto(AnimalhistoPK animalhistoPK) {
        this.animalhistoPK = animalhistoPK;
    }

    public Animalhisto(AnimalhistoPK animalhistoPK, Date dateNaissance,
            short especeId, String utilisateurHisto, String evenementHisto) {
        this.animalhistoPK = animalhistoPK;
        this.dateNaissance = dateNaissance;
        this.especeId = especeId;
        this.utilisateurHisto = utilisateurHisto;
        this.evenementHisto = evenementHisto;
    }

    public Animalhisto(short id, Date dateHisto) {
        this.animalhistoPK = new AnimalhistoPK(id, dateHisto);
    }

    public AnimalhistoPK getAnimalhistoPK() {
        return animalhistoPK;
    }

    public void setAnimalhistoPK(AnimalhistoPK animalhistoPK) {
        this.animalhistoPK = animalhistoPK;
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

    public short getEspeceId() {
        return especeId;
    }

    public void setEspeceId(short especeId) {
        this.especeId = especeId;
    }

    public Short getRaceId() {
        return raceId;
    }

    public void setRaceId(Short raceId) {
        this.raceId = raceId;
    }

    public Short getMereId() {
        return mereId;
    }

    public void setMereId(Short mereId) {
        this.mereId = mereId;
    }

    public Short getPereId() {
        return pereId;
    }

    public void setPereId(Short pereId) {
        this.pereId = pereId;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getUtilisateurHisto() {
        return utilisateurHisto;
    }

    public void setUtilisateurHisto(String utilisateurHisto) {
        this.utilisateurHisto = utilisateurHisto;
    }

    public String getEvenementHisto() {
        return evenementHisto;
    }

    public void setEvenementHisto(String evenementHisto) {
        this.evenementHisto = evenementHisto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (animalhistoPK != null ? animalhistoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Animalhisto)) {
            return false;
        }
        Animalhisto other = (Animalhisto) object;
        if ((this.animalhistoPK == null && other.animalhistoPK != null) ||
                (this.animalhistoPK != null &&
                !this.animalhistoPK.equals(other.animalhistoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.Animalhisto[ animalhistoPK=" + animalhistoPK +
                " ]";
    }

}
