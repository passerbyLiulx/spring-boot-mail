package com.example.mail.mapper;

import com.example.mail.dao.JobInfoDao;
import com.example.mail.model.JobInfoModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface JobInfoMapper {

	/**
	 * 添加定时任务信息
	 * @param jobInfoDao
	 * @return
	 */
	@Insert("insert into job_info("
		+ "job_id, task_name, task_type, expression, expression_desc, "
		+ "task_cycle, run_node, open_state, delete_state, create_time) values ("
		+ "#{jobId}, #{taskName}, #{taskPath}, #{expression}, #{expressionDesc}, "
		+ "#{taskCycle}, #{runNode}, #{openState}, #{deleteState}, now())")
	public int addJobInfo(JobInfoDao jobInfoDao);

	/**
	 * 修改定时任务信息
	 * @param jobInfoDao
	 * @return
	 */
	@Update("<script> update job_info"
	+ "<set>"
	+ "<if test='taskName != null'>task_name = #{taskName}, </if>"
	+ "<if test='taskPath != null'>task_path = #{taskPath}, </if>"
	+ "<if test='expression != null'>expression = #{expression}, </if>"
	+ "<if test='expressionDesc != null'>expression_desc = #{expressionDesc}, </if>"
	+ "<if test='taskCycle != null'>task_cycle = #{taskCycle}, </if>"
	+ "<if test='runNode != null'>run_node = #{runNode}, </if>"
	+ "<if test='openState != null'>open_state = #{openState}, </if>"
	+ " update_time = now()"
	+ "</set>"

	+ " where job_id = #{jobId}</script>")
	public int updateJobInfo(JobInfoDao jobInfoDao);

	/**
	 * 删除定时任务信息
	 * @param jobId
	 * @return
	 */
	@Update("update job_info set delete_state = 1 where job_id = #{jobId}")
	public int deleteJobInfo(String jobId);

	/**
	 * 根据Id获取定时任务信息
	 * @param jobId
	 * @return
	 */
	@Select("select * from job_info where job_id = #{jobId} and delete_state = 0")
	public JobInfoDao getJobInfoById(String jobId);

	/**
	 * 获取定时任务集合信息
	 * @return
	 */
	@Select("select * from job_info where delete_state = 0")
	public List<JobInfoDao> jobInfoList();

	/**
	 * 获取开启的定时任务集合信息
	 * @return
	 */
	@Select("select * from job_info where delete_state = 0 and open_state = 1")
	public List<JobInfoDao> jobInfoListByOpen();

	/**
	 * 根据Id获取定时任务数量
	 * @return
	 */
	@Select("select count(*) from job_info where job_id = #{jobId} and delete_state = 0")
	public int countJobInfoById(String jobId);

	/**
	 * 获取定时任务分页信息
	 * @param jobInfoModel
	 * @return
	 */
	@Select("<script>"
			+ "select * from job_info where is_deleted = 0 "
			+ "<if test='taskType!=null'>and task_type = #{taskType} </if>"
			+ "</script>")
	public List<JobInfoModel> jobInfoListByPageCondition(JobInfoModel jobInfoModel);
}
