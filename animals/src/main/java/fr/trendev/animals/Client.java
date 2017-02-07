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
import javax.persistence.Lob;
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
@Table(name = "Client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
    ,
        @NamedQuery(name = "Client.findById",
            query = "SELECT c FROM Client c WHERE c.id = :id")
    ,
        @NamedQuery(name = "Client.findByNom",
            query = "SELECT c FROM Client c WHERE c.nom = :nom")
    ,
        @NamedQuery(name = "Client.findByPrenom",
            query = "SELECT c FROM Client c WHERE c.prenom = :prenom")
    ,
        @NamedQuery(name = "Client.findByAdresse",
            query = "SELECT c FROM Client c WHERE c.adresse = :adresse")
    ,
        @NamedQuery(name = "Client.findByCodePostal",
            query = "SELECT c FROM Client c WHERE c.codePostal = :codePostal")
    ,
        @NamedQuery(name = "Client.findByVille",
            query = "SELECT c FROM Client c WHERE c.ville = :ville")
    ,
        @NamedQuery(name = "Client.findByPays",
            query = "SELECT c FROM Client c WHERE c.pays = :pays")
    ,
        @NamedQuery(name = "Client.findByDateNaissance",
            query = "SELECT c FROM Client c WHERE c.dateNaissance = :dateNaissance")})
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 200)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 6)
    @Column(name = "code_postal")
    private String codePostal;
    @Size(max = 60)
    @Column(name = "ville")
    private String ville;
    @Size(max = 60)
    @Column(name = "pays")
    private String pays;
    @Lob
    @Column(name = "email")
    private byte[] email;
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private Collection<Adoption> adoptionCollection;

    public Client() {
    }

    public Client(Short id) {
        this.id = id;
    }

    public Client(Short id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public byte[] getEmail() {
        return email;
    }

    public void setEmail(byte[] email) {
        this.email = email;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @XmlTransient
    public Collection<Adoption> getAdoptionCollection() {
        return adoptionCollection;
    }

    public void setAdoptionCollection(Collection<Adoption> adoptionCollection) {
        this.adoptionCollection = adoptionCollection;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) ||
                (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.trendev.animals.Client[ id=" + id + " ]";
    }

}
