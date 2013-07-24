package th.co.aerothai.callservice.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import th.co.aerothai.callservice.model.hr.PersonalInfo;

@Entity
@Table(name="SYSTEMUSER")
@GenericGenerator(strategy="th.co.aerothai.callservice.utils.HibernateCurrentTimeIDGenerator", name="IDGENERATOR")
public class SystemUser implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator="IDGENERATOR")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STAFFCODE", referencedColumnName="STAFFCODE")
	private PersonalInfo staff;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="user")
	private List<Authorization> roles;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BOSS", referencedColumnName="STAFFCODE")
	private PersonalInfo boss;
	
	@Column(name="ISACTIVE")
	private boolean isActive = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PersonalInfo getStaff() {
		return staff;
	}

	public void setStaff(PersonalInfo staff) {
		this.staff = staff;
	}

	public List<Authorization> getRoles() {
		return roles;
	}

	public void setRoles(List<Authorization> roles) {
		this.roles = roles;
	}

	public PersonalInfo getBoss() {
		return boss;
	}

	public void setBoss(PersonalInfo boss) {
		this.boss = boss;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
