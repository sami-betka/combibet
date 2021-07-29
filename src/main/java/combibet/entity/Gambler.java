package combibet.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
//@Data
//@Getter
//@Setter
@SequenceGenerator(
		  name = "GAMBLER_SEQ_GENERATOR",
		  sequenceName = "GAMBLER_SEQ",
		  initialValue = 1, allocationSize = 1)
public class Gambler {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GAMBLER_SEQ_GENERATOR")

	private Long id;
	
	private String userName;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	@OneToMany(mappedBy="gambler")
	private List <HorseRacingBet> bets = new ArrayList<HorseRacingBet>();
	
	@OneToMany(mappedBy="gambler")
	private List <Combi> combis = new ArrayList<Combi>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<HorseRacingBet> getBets() {
		return bets;
	}

	public void setBets(List<HorseRacingBet> bets) {
		this.bets = bets;
	}

	public List<Combi> getCombis() {
		return combis;
	}

	public void setCombis(List<Combi> combis) {
		this.combis = combis;
	}
	
	
}
