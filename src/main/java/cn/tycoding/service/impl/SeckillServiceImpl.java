package cn.tycoding.service.impl;

import cn.tycoding.dto.Exposer;
import cn.tycoding.dto.SeckillExecution;
import cn.tycoding.entity.Seckill;
import cn.tycoding.entity.SeckillOrder;
import cn.tycoding.enums.SeckillStatEnum;
import cn.tycoding.exception.RepeatKillException;
import cn.tycoding.exception.SeckillCloseException;
import cn.tycoding.exception.SeckillException;
import cn.tycoding.mapper.SeckillMapper;
import cn.tycoding.mapper.SeckillOrderMapper;
import cn.tycoding.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService{
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    private final String salt="sjajaspu-i-2jrfm;sd";//设置盐值字符串，随便定义，用于混淆MD5值
    private final String key="seckill";//设置秒杀redis缓存的key

    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Seckill> findAll(){
        redisTemplate.delete("seckill");
        List<Seckill> seckillList=redisTemplate.boundHashOps("seckill").values();
        if(seckillList==null||seckillList.size()==0){
            //查询缓存中没有秒杀列表数据
            //查询数据库中没有秒杀列表数据，并将列表数据循环放入redis缓存中
            seckillList=seckillMapper.findAll();
            for(Seckill seckill:seckillList){
                //将秒杀列表数据依次存放到redis缓存中，key:秒杀表的ID值；value:秒杀商品数据
                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(),seckill);
                logger.info("findAll -> 从数据库中读取放入缓存中");
            }
        }else{
            logger.info("findAll  -> 从缓存中读取");
        }
        return seckillList;
    }
    @Override
    public Seckill findById(long seckillId){
        return seckillMapper.findById(seckillId);
    }
    @Override
    public Exposer exportSeckillUrl(long seckillId){
        Seckill seckill=(Seckill)redisTemplate.boundHashOps(key).get(seckillId);
        if(seckill==null) {
            seckill = seckillMapper.findById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                redisTemplate.boundHashOps(key).put(seckill.getSeckillId(), seckill);
                logger.info("RedisTemplate -> 从数据库中读取并放入缓存中");
            }
        }else {
            logger.info("RedisTemplate -> 从缓存中读取");
        }
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
            //获取系统时间
        Date nowTime=new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
                return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5=getMD5(seckillId);//转换特定字符串的过程，不可逆的算法
        return new Exposer(true,md5,seckillId);
    }
        //生成MD5值
     private String getMD5(Long seckillId){
        String base=seckillId+"/"+salt;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, BigDecimal money,long usePhone,String md5)
        throws SeckillException,RepeatKillException,SeckillCloseException {
        if(md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime=new Date();//执行秒杀逻辑，1减库存，2存储秒杀订单
        try{
            int insertCount=seckillOrderMapper.insertOrder(seckillId,money,usePhone);
            if(insertCount<=0){
                throw new RepeatKillException("seckill repeated");
            }else{
                int updateCount =seckillMapper.reduceStock(seckillId,nowTime);
                if(updateCount<=0){
                    throw new SeckillCloseException("seckill is closed");
                }else{//秒杀成功
                    SeckillOrder seckillOrder=seckillOrderMapper.findById(seckillId);
                    //更新缓存
                    Seckill seckill=(Seckill) redisTemplate.boundHashOps(key).get(seckillId);
                    seckill.setStockCount(seckill.getSeckillId()-1);
                    redisTemplate.boundHashOps(key).put(seckillId,seckill);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,seckillOrder);
                }
            }
        }catch (SeckillCloseException e){
            throw e;
        }catch(RepeatKillException e){
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }
}
