package cn.tycoding.mapper;

import cn.tycoding.entity.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SeckillMapper {
    List<Seckill> findAll();//查询所有秒杀商品的记录信息
    Seckill findById(long id);//根据主键查询当前秒杀商品的数据
    int reduceStock(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);//减库存



}
