package data.promotiondata;

import dataService.promotiondataService.PromotiondataService;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import po.MemberPromotionPO;
import util.ResultMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MemberPromotionData implements PromotiondataService<MemberPromotionPO> {
    @Override
    public int getDayId() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        int resultID;
        try(SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            MemberPromotionPOMapper mapper = session.getMapper(MemberPromotionPOMapper.class);
            resultID = mapper.getDayId(today);
            session.commit();
        }
        return resultID;
    }

    @Override
    public ResultMessage insert(MemberPromotionPO promotionPO) {
        promotionPO.setCreateTime(LocalDateTime.now());
        promotionPO.setLastModifiedTime(LocalDateTime.now());

        try(SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            MemberPromotionPOMapper mapper = session.getMapper(MemberPromotionPOMapper.class);
            mapper.insert(promotionPO);
            session.commit();
        }
        return ResultMessage.SUCCESS;
    }

    @Override
    public ResultMessage update(MemberPromotionPO promotionPO) {
        promotionPO.setLastModifiedTime(LocalDateTime.now());

        try(SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            MemberPromotionPOMapper mapper = session.getMapper(MemberPromotionPOMapper.class);
            mapper.update(promotionPO);
            session.commit();
        }
        return ResultMessage.SUCCESS;
    }

    @Override
    public ResultMessage delete(MemberPromotionPO promotionPO) {
        try(SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            MemberPromotionPOMapper mapper = session.getMapper(MemberPromotionPOMapper.class);
            mapper.delete(promotionPO);
            session.commit();
        }
        return ResultMessage.SUCCESS;
    }


    @Override
    public List<MemberPromotionPO> selectInEffect() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDayZero = LocalDateTime.of(now.plusDays(1).toLocalDate(), LocalTime.MIN);

        List<MemberPromotionPO> resultList;

        try(SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            MemberPromotionPOMapper mapper = session.getMapper(MemberPromotionPOMapper.class);
            resultList = mapper.selectInEffect(now, nextDayZero);
            session.commit();
        }
        return resultList;
    }



    // for test
    public List<MemberPromotionPO> getAll() {
        List<MemberPromotionPO> resultList;
        try(SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession()) {
            MemberPromotionPOMapper mapper = session.getMapper(MemberPromotionPOMapper.class);
            resultList = mapper.getAll();
            session.commit();
        }
        return resultList;
    }
}
