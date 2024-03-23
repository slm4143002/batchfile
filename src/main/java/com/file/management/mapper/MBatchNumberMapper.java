package com.file.management.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.file.management.entity.MBatchNumber;

public interface MBatchNumberMapper extends BaseMapper<MBatchNumber> {

    @Select("select m.batch_number from m_batch_number m" +
            " inner join t_preparatory_detail t on m.batch_number = t.batch_number" +
            " where m.batch_number = #{mBatchNumber} and DATE(m.write_date) = DATE(t.write_date) and DATE(m.write_date) = CURDATE()")
    List<MBatchNumber> selectNum(@Param("mBatchNumber") String mBatchNumber);

    @Select("select m.batch_number from m_batch_number m where m.batch_number = #{mBatchNumber} and DATE(m.write_date) = CURDATE()")
    MBatchNumber selectRepeat(@Param("mBatchNumber") String mBatchNumber);

}