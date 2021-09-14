select coalesce(t_comment_sum.vid,t_like_sum.vid,t_follow_sum.vid,t_collect_sum.vid,t_share_sum.vid) as vid,
like_cnt_fe,comment_cnt_fe,follow_cnt_fe,collect_cnt_fe,share_cnt_fe
from
(
	select vid, sum(like_cnt_fe) as like_cnt_fe
	from t_like
	group by vid
) as t_like_sum
full join
(
	select vid, sum(comment_cnt_fe) as comment_cnt_fe
	from t_comment
	group by vid
) as t_comment_sum
on t_like_sum.vid=t_comment_sum.vid
full join
(
	select vid, sum(follow_cnt_fe) as follow_cnt_fe
	from t_follow
	group by vid
) as t_follow_sum
on t_like_sum.vid=t_follow_sum.vid
full join
(
	select vid, sum(collect_cnt_fe) as collect_cnt_fe
	from t_collect
	group by vid
) as t_collect_sum
on t_like_sum.vid=t_collect_sum.vid
full join
(
	select vid, sum(share_cnt_fe) as share_cnt_fe
	from t_share
	group by vid
) as t_share_sum
on t_like_sum.vid=t_share_sum.vid