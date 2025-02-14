package com.file.management.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("m_batch_number")
public class MBatchNumber {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.id
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
    @TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.batch_number
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
	private String batchNumber;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.machine_category_name
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
	private String machineCategoryName;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.machine_count
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
	private Integer machineCount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.car_count
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
	private Integer carCount;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.write_date
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date writeDate;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.delete_flg
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
	private String deleteFlg;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column m_batch_number.create_date
	 * @mbg.generated  Thu Feb 29 20:48:52 CST 2024
	 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getMachineCategoryName() {
		return machineCategoryName;
	}

	public void setMachineCategoryName(String machineCategoryName) {
		this.machineCategoryName = machineCategoryName;
	}

	public Integer getMachineCount() {
		return machineCount;
	}

	public void setMachineCount(Integer machineCount) {
		this.machineCount = machineCount;
	}

	public Integer getCarCount() {
		return carCount;
	}

	public void setCarCount(Integer carCount) {
		this.carCount = carCount;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public String getDeleteFlg() {
		return deleteFlg;
	}

	public void setDeleteFlg(String deleteFlg) {
		this.deleteFlg = deleteFlg;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}