--压缩数据，同一个item的数据根据时间戳取最新--
select feed_id,comment_num as delta_comment_num
from(
	select f.*, row_number() over (partition by feed_id order by ftime desc) rn
	from(
		select ftime,case when root_feed_id='0' then feed_id else root_feed_id end as feed_id,root_feed_comment_cnt as comment_num
   from t_8976
   where length(root_feed_id)<>0 and length(root_feed_comment_cnt)<>0
   and cast(root_feed_comment_cnt as int) is not null
		and eid in ('comment_publish','reply_publish')
	) as f
) as t
where t.rn=1

--增量数据与全量数据进行merge，如果两者都有取增量--
select coalesce(t_batch.feed_id,t_delta.feed_id) as feedid, case when delta_comment_num is not null then delta_comment_num  else batch_comment_num end as comment_num
from t_batch
full join t_delta on t_batch.feed_id=t_delta.feed_id

--使用子查询进一步计算百分比，可以使用相关udf进行精度处理--
select joined_count/order_count as join_rate
from(
 select count(1) as total,
   count(t_order.orderId) as order_count,
   count(t_payment.orderId) as payment_count,
   count(case when t_payment.orderId is not null and t_order.orderId is not null then 1 else null end) as joined_count
 from t_order
 full join t_payment
 on t_order.orderId=t_payment.orderId
)