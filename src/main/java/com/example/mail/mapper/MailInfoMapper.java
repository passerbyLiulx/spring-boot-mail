package com.example.mail.mapper;

import com.example.mail.dao.MailInfoDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailInfoMapper {

    /**
     * 保存邮件信息
     * @param mailInfoDao
     * @return
     */
    public int addMailInfo(MailInfoDao mailInfoDao);

    /**
     * 修改邮件信息
     * @param mailInfoDao
     * @return
     */
    public int updateMailInfo(MailInfoDao mailInfoDao);

    /**
     * 获取邮件信息
     * @param mailId
     * @return
     */
    public MailInfoDao getMailInfoByMailId(String mailId);

    /**
     * 获取发送失败的邮件信息集合
     * @return
     */
    public List<MailInfoDao> mailInfoListBySendState(Integer sendState);
}
