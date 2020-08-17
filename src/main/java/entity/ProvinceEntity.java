package entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@NamedNativeQueries({
//	@NamedNativeQuery(name="callinsert", query = "call insertprovince(:provinceName)", resultClass = ProvinceEntity.class)
//})
@Entity
@Table(name = "province", uniqueConstraints = { @UniqueConstraint(columnNames = "province_id") })
@Data
@NoArgsConstructor
public class ProvinceEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "province_id", unique = true, nullable = false)
	private int id;
	@Column(name = "province_name", unique = true, nullable = false)
	private String provinceName;

	public ProvinceEntity(String provinceName) {
		super();
		this.provinceName = provinceName;
	}

}
