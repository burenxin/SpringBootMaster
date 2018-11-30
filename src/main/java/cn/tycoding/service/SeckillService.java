package cn.tycoding.service;

import cn.tycoding.dto.Exposer;
import cn.tycoding.dto.SeckillExecution;
import cn.tycoding.entity.Seckill;
import cn.tycoding.exception.RepeatKillException;
import cn.tycoding.exception.SeckillCloseException;
import cn.tycoding.exception.SeckillException;

import java.math.BigDecimal;
import java.util.List;

public interface SeckillService {
    List<Seckill> findAll();
    Seckill findById(long seckillId);
    Exposer exportSeckillUrl(long seckillId);//秒杀开始时暴露秒杀的地址，否则输出系统时间和秒杀地址
    //执行秒杀的操作
    SeckillExecution executeSeckill(long seckillId, BigDecimal money,long userPhone,String md5)
        throws SeckillException,RepeatKillException,SeckillCloseException;
}
